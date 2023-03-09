package com.eyas.framework.impl;

import com.eyas.framework.EmptyUtil;
import com.eyas.framework.GsonUtil;
import com.eyas.framework.constant.SystemConstant;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import com.eyas.framework.intf.EyasFrameworkRedisService;
import com.eyas.framework.middle.EyasFrameworkMiddle;
import com.eyas.framework.service.impl.EyasFrameworkServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EyasFrameworkRedisServiceImpl<Dto,D,Q> extends EyasFrameworkServiceImpl<Dto,D,Q> implements EyasFrameworkRedisService<Dto,D,Q> {


    private final RedissonServiceImpl redisService;

    private final CacheUtils cacheUtils;


    public EyasFrameworkRedisServiceImpl(EyasFrameworkMiddle<D, Q> eyasFrameworkMiddle,
                                         RedissonServiceImpl redisService, CacheUtils cacheUtils) {
        super(eyasFrameworkMiddle);
        this.redisService = redisService;
        this.cacheUtils = cacheUtils;
    }

    @Override
    public Integer createRedisElement(Dto dto, String key, long time) {
        Integer insert = super.insert(dto);
        if (1 == insert) {
            // 缓存
            if (EmptyUtil.isEmpty(time)) {
                time = redisService.getElementCacheTimeout();
            }
            this.redisService.setStrTime(key, GsonUtil.objectToJson(dto), time, TimeUnit.MILLISECONDS);
        }
        return insert;
    }

    @Override
    public Integer updateRedisElement(Dto dto, String key, long time, Long id) {
        RLock rLock = redisService.redissonWriteLock(key);
        Integer update;
        try {
            // 新增数据--防止双写不一致
            update = super.updateByLock(dto, id);
            // 更新缓存
            this.redisService.setStrTime(key, GsonUtil.objectToJson(dto), time, TimeUnit.MILLISECONDS);
            this.cacheUtils.putAndUpdateCache(key, dto);
        } catch (Exception e) {
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.UPDATE_DATA_FAIL, "热点数据更新有误!");
        } finally {
            this.redisService.redissonReadWriteUnLock(rLock);
        }
        return update;
    }

    /**
     * 高可用热点数据双写一致性
     * 逻辑
     * 1、解决缓存穿透问题
     * 2、解决缓存失效问题
     * 3、解决读写不一致的问题
     * 4、增加读写锁提高获取数据的性能
     * 实现逻辑
     * 1、获取缓存数据:
     * 从一层redis增加三层并且可配置
     * --①jvm层使用ConcurrentHashMap(注意大小分配)
     * ① 使用caffeine作为本地一级缓存
     * ② 布隆过滤器作为二级缓存
     * ③ redis--如果查询的数据为空。缓存短暂的空数据
     * 2、DCL双重检查锁机制-防止缓存穿透
     * 分布式锁，在缓存没有数据的时候，只允许一个线程进来读数据库，其他的等待(这边需要注意一下失效时间问题)
     * 1、第二个线程进来就不会继续落到数据库了，失效时间控制的合理，会统一释放用户。
     * 2、失效时间根据业务需要调整，会导致部分线程获取不到锁的情况，不然会等待(总有一个线程持有一把锁，有性能消耗)
     * 3、增加读写锁
     * ①更新数据的时候使用写锁
     * ②查询数据的时候使用读锁
     * 当两个线程的mode都是read read的时候，支持并发访问，提高性能
     *
     * 优化方向:
     * ①自旋锁会占据线程,希望通过await那种方式去释放线程，然后由释放锁的线程主动去释放阻塞线程
     * ②提高一定的并发度，可以增加redis分段锁来缓解并发压力
     *
     * 测试结果:
     * v1-2021-03-24
     * 并发10个，效果达到，但是有部分线程未能获取到数据(可能跟DCL加锁有关系)，存在bug
     * v2-2022-05-24
     * tryLock需要自己调控一下是否需要再次获得锁的逻辑。可以充分利用一下线程的资源
     * lock把线程调度交给cpu，都可以，是否需要增加一个lock后续考虑
     * 并发100个，达到效果
     * v3-2022-06-2
     * 优化tryLock自旋逻辑，尝试二次获取数据
     * 使用信号量放一部分线程尝试去拿一下数据。拿不到就再自旋，拿到了就返回
     * v4-2022-07-06
     * juc包无法实现分布式场景，目前设置了自旋次数超过核心数强制out来做处理
     * 后续版本可以增加类似阻塞队列非占线程的方式来处理自旋逻辑。
     * 比如可以使用redis的队列，把请求丢进队列，然后释放的时候从队列里面brPop
     * v5-2022-07-11
     * 优化本地的map缓存--设置阈值长度32.超过的忽略
     * v6-2022-08-09
     * 增加本地缓存技术-caffeineCache-用来优化本地一级缓存
     * v6-2022-09-04
     * 把释放锁逻辑调整到业务快跟try{}catch{}块里面释放
     * 因为如果存在业务异常(获取锁次数超过核心数，人为结束),依然会进finally。
     *
     * @param element 缓存key
     * @param waitTime 线程等锁时间(不宜超过10s)，可以为空(默认10s)
     * @param failureTime key失效时间
     * @param elementKeyId 缓存key对应的数据id-用来获取数据库数据
     * @return Object
     *
     */
    @Override
    public Object getRedisElement(String element, Long waitTime, Long failureTime, String elementKeyId, TimeUnit timeUnit){
        String elementKey = element + ":key";
        String elementReadWriteKey = element + ":readWriteKey";
        Object object = this.redisService.getElementFromCache(element);
        if (null != object){
            // 如果不是空返回
            return object;
        }
        // DCL-双重检查锁--防止缓存失效
        // 这个时间需要根据情况设置-可以为空，默认30s
        boolean lockFlag = false;
        int count = 0;
        while (!lockFlag){
            lockFlag = this.redisService.redissonTryLock(elementKey, waitTime, timeUnit);
            // 自旋
            if (!lockFlag) {
                count++;
                // 自旋锁-cpu核心次数
                if (count >= SystemConstant.PROCESSORS) {
                    break;
                }
            }
        }
        if (!lockFlag){
            // 自旋一定的次数如果还未获取到锁，那么我就释放锁，返回空对象
            // 防止自旋异常，增加一次获取锁尝试
            lockFlag = this.redisService.redissonTryLock(elementKey, waitTime, timeUnit);
            // 依然失败就返回结束
            if (!lockFlag) {
                return new Object();
            }
        }
        try {
            // 继续查询一下缓存
            object = this.redisService.getElementFromCache(element);
            if (null != object){
                // 如果不是空返回
                return object;
            }
            // 加读锁--为了提高性能
            RLock rLock = this.redisService.redissonReadLock(elementReadWriteKey);
            try {
                // 查询数据库--调用父类方法
//                object = super.getInfoById(Long.valueOf(elementKeyId));
                // 模拟设置
                object = "1212212";
                if (EmptyUtil.isNotEmpty(object)) {
                    // 缓存redis
                    if (EmptyUtil.isEmpty(failureTime)) {
                        this.redisService.setStr(element, GsonUtil.objectToJson(object));
                    }else{
                        this.redisService.setStrTime(element, GsonUtil.objectToJson(object), failureTime, timeUnit);
                    }
                    // 缓存本地map
                    this.cacheUtils.putAndUpdateCache(element, object);
                }else{
                    // 防止缓存穿透--缓存空数据
                    this.redisService.setStrTime(element, this.redisService.getStr(element), this.redisService.genEmptyCacheTimeout().longValue(), timeUnit);
                }
            }catch (Exception e){
                throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.SYSTEM_ERROR, "获取商品失败!");
            }finally {
                /**
                 * 等锁进程如果业务判断获取锁失败，依然会进finally去释放锁，不合理
                 */
                this.redisService.redissonReadWriteUnLock(rLock);
            }
        }catch(Exception e){
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.SYSTEM_ERROR, "redisson tryLock fail");
        } finally {
            this.redisService.redissonUnLock(elementKey);
        }
        return object;
    }

    @Override
    public Object getRedisElementLogs(String element, Long waitTime, Long failureTime, String elementKeyId, TimeUnit timeUnit){
        String elementKey = element + ":key";
        String elementReadWriteKey = element + ":readWriteKey";
        Object object = this.redisService.getElementFromCache(element);
        if (null != object){
            log.info(Thread.currentThread().getName() + "线程--->获取到数据了！");
            // 如果不是空返回
            return object;
        }
        log.info(Thread.currentThread().getName() + "线程--->没有获取数据了！");
        // DCL-双重检查锁--防止缓存失效
        // 这个时间需要根据情况设置-可以为空，默认30s
        boolean lockFlag = false;
        int count = 0;
        while (!lockFlag){
            lockFlag = this.redisService.redissonTryLock(elementKey, waitTime, timeUnit);
            // 自旋
            if (!lockFlag) {
                log.info(Thread.currentThread().getName() + "线程--->没有获取到锁了！");
                count++;
                // 自旋锁-cpu核心次数
                if (count >= SystemConstant.PROCESSORS) {
                    log.info(Thread.currentThread().getName() + "线程--->超过了核心数，拒绝！");
                    break;
                }
            }
        }
        if (!lockFlag){
            // 自旋一定的次数如果还未获取到锁，那么我就释放锁，返回空对象
            // 防止自旋异常，增加一次获取锁尝试
            lockFlag = this.redisService.redissonTryLock(elementKey, waitTime, timeUnit);
            // 依然失败就返回结束
            if (!lockFlag) {
                log.info(Thread.currentThread().getName() + "线程--->获取锁失败！");
                return new Object();
            }
        }
        try {
            log.info(Thread.currentThread().getName() + "线程--->加锁成功！");
            log.info(Thread.currentThread().getName() + "线程--->加锁成功！" + "自旋数量:" + count);
            // 继续查询一下缓存
            object = this.redisService.getElementFromCache(element);
            if (null != object){
                // 如果不是空返回
                log.info(Thread.currentThread().getName() + "线程--->获取数据成功！");
                return object;
            }
            // 加读锁--为了提高性能
            RLock rLock = this.redisService.redissonReadLock(elementReadWriteKey);
            try {
                // 查询数据库--调用父类方法
//                object = super.getInfoById(Long.valueOf(elementKeyId));
                // 模拟设置
                object = "1212212";
                log.info(Thread.currentThread().getName() + "线程--->打到数据库了！！！");
                if (EmptyUtil.isNotEmpty(object)) {
                    // 缓存redis
                    if (EmptyUtil.isEmpty(failureTime)) {
                        this.redisService.setStr(element, GsonUtil.objectToJson(object));
                    }else{
                        this.redisService.setStrTime(element, GsonUtil.objectToJson(object), failureTime, timeUnit);
                    }
                    // 缓存本地map
                    this.cacheUtils.putAndUpdateCache(element, object);
                }else{
                    // 防止缓存穿透--缓存空数据
                    this.redisService.setStrTime(element, this.redisService.getStr(element), this.redisService.genEmptyCacheTimeout().longValue(), timeUnit);
                }
            }catch (Exception e){
                throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.SYSTEM_ERROR, "获取商品失败!");
            }finally {
                log.info(Thread.currentThread().getName() + "线程--->读写锁释放锁");
                this.redisService.redissonReadWriteUnLock(rLock);
            }
        }catch(Exception e){
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.SYSTEM_ERROR, "redisson tryLock fail");
        }finally {
            log.info(Thread.currentThread().getName() + "线程--->自旋锁释放锁");
            this.redisService.redissonUnLock(elementKey);
        }
        return object;
    }

    @Override
    public Object getRedisElement(String element, Long waitTime, String elementKeyId, TimeUnit timeUnit){
        return this.getRedisElement(element, waitTime, null, elementKeyId, timeUnit);
    }
}
