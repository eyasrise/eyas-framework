package com.eyas.framework.impl;

import com.eyas.framework.EmptyUtil;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.constraint.RedisKeyEnumConstraintInterface;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import com.eyas.framework.intf.RedissonService;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class RedissonServiceImpl implements RedissonService {

    public static final Integer PRODUCT_CACHE_TIMEOUT = 24;

    public static final String EMPTY_CACHE = "{}";

    public static final Integer PRODUCT_LOCK_DEFAULT_WAIT_TIME = 1000;

    /**
     * 锁失效时间
     */
    public static final Integer PRODUCT_CACHE_LEASE_TIME = 60000;

    private final RedissonClient redissonClient;

    private final CacheUtils cacheUtils;

    public RedissonServiceImpl(RedissonClient redissonClient, CacheUtils cacheUtils) {
        this.redissonClient = redissonClient;
        this.cacheUtils = cacheUtils;
    }

    /**
     * 读取缓存中的字符串，永久有效
     *
     * @param key 缓存key
     * @return 字符串
     */
    public String getStr(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 缓存字符串
     *
     * @param key
     * @param value
     */
    public void setStr(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     *
     *
     * @param key
     * @param value
     * @param timeToLive
     * @param timeUnit
     */
    public void setStrTime(String key, String value, Long timeToLive, TimeUnit timeUnit) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, timeToLive, timeUnit);
    }

    /**
     * 读取缓存中的字符串，永久有效
     *
     * @param redisKeyEnumInterface 缓存key
     * @return 字符串
     */
    @Override
    public String getStr(RedisKeyEnumConstraintInterface redisKeyEnumInterface) {
        return this.getStr(redisKeyEnumInterface.redisKeyValue().toString());
    }

    /**
     * 缓存字符串
     *
     * @param redisKeyEnumInterface redisKey枚举
     * @param value 缓存数据
     */
    @Override
    public void setStr(RedisKeyEnumConstraintInterface redisKeyEnumInterface, String value) {
        this.setStr(redisKeyEnumInterface.redisKeyValue().toString(), value);
    }

    @Override
    public void setStrTime(RedisKeyEnumConstraintInterface redisKeyEnumInterface, String value, Long timeToLive, TimeUnit timeUnit) {
        this.setStrTime(redisKeyEnumInterface.redisKeyValue().toString(), value, timeToLive, timeUnit);
    }


    //===============================redisson=================================

    /**
     * 锁等待时间如果为空，默认设置1s一次;
     * 锁等待时间不宜大于看门狗的触发时间(续期时间/3),如果锁等待时间大于看门狗的触发时间，默认修改成看门狗的触发时间/2
     * 比如:redisson默认的续期时间是30s,看门狗的触发时间是30s/3=10s
     * 那么如果锁等待时间设置超过了10s，其实不合理，这种场景项目默认设置成10s/2=5s
     * leaseTime:锁失效时间，默认为设置为60s，看门狗运行6次，锁30s默认续期一次
     * @see PRODUCT_CACHE_LEASE_TIME 60s
     * @param key 锁key
     * @param waitTime 线程等待拿锁时间
     * @return 拿锁结果
     */
    @Override
    public boolean redissonTryLock(String key, long waitTime, TimeUnit timeUnit){
        RLock hotCacheLock = redissonClient.getLock(key);
        try {
            if (EmptyUtil.isEmpty(waitTime)){
                waitTime = PRODUCT_LOCK_DEFAULT_WAIT_TIME;
            }
            // 获取看门狗时间
            long lockWatchdogTimeout = redissonClient.getConfig().getLockWatchdogTimeout();
            if (EmptyUtil.isEmpty(lockWatchdogTimeout)){
                redissonClient.getConfig().setLockWatchdogTimeout(30000L);
                lockWatchdogTimeout = 30000L;
            }
            // 判断时间：等待时间理论上不应该超过看门狗的触发时间也就是续期时间/3
            // 默认给续期时间/3的基础上再折一半
            // 获取一下等待时间
            long waitTimeMs = timeUnit.toMillis(waitTime);
            if (waitTimeMs > lockWatchdogTimeout/3){
                waitTimeMs = lockWatchdogTimeout/6;
            }
            return hotCacheLock.tryLock(waitTimeMs, PRODUCT_CACHE_LEASE_TIME, timeUnit);
        } catch (InterruptedException e) {
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.SYSTEM_ERROR, "redisson tryLock fail");
        }
    }

    @Override
    public void redissonUnLock(String key){
        RLock hotCacheLock = redissonClient.getLock(key);
        hotCacheLock.unlock();
    }

    public Object getElementFromCache(String key){
        return getElementFromCache(key, false);
    }

    public Object getElementFromCache(String key, boolean bloomFilterExist){
        // 本地获取--这个map需要热数据维护
        Object element = cacheUtils.getObjCacheByKey(key, String.class);
        if (EmptyUtil.isNotEmpty(element)){
            return element;
        }
        // 布隆过滤器获取--按需设置--针对缓存穿透
        if (bloomFilterExist){
            String bloomFilterKey = "bloomFilterKey";
            // 判断布隆过滤器是否存在
            Object bloomFilterValue = cacheUtils.getObjCacheByKey(bloomFilterKey, String.class);
            // map会重启消失，所以map应该由热数据去维护
            if (EmptyUtil.isEmpty(bloomFilterValue)){
                // 如果是空初始化
                RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterKey);
                //初始化布隆过滤器：预计元素为100000000L,误差率为3%,根据这两个参数会计算出底层的bit数组大小
                bloomFilter.tryInit(100000L,0.03);
                // 把数据添加到bloomFilter
                bloomFilter.add(key);
                // 把bloomFilter塞入map
                this.cacheUtils.putAndUpdateCache(bloomFilterKey, "bloomFilterKey");
            }
            // 如果不是空--开始布隆过滤器逻辑
            RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterKey);
            boolean elementExist = bloomFilter.contains(key);
            if (!elementExist){
                // 如果元素不存在-直接返回
                return null;
            }
            // 如果元素存在布隆过滤器-继续查看redis
        }
        // redis获取
        Object object = this.getStr(key);
        if (EmptyUtil.isNotEmpty(object)){
            // 避免缓存穿透
            // 第一步如果数据是空，返回空对象并且续期
            if (EMPTY_CACHE.equals(object)){
                // 空数据续期
                this.setStrTime(key, this.getStr(key), genEmptyCacheTimeout().longValue(), TimeUnit.MILLISECONDS);
                return new Object();
            }
            // 如果不为空
            // 相对热数据续期--增加随机数--避免缓存失效
            this.setStrTime(key, this.getStr(key), genEmptyCacheTimeout().longValue(), TimeUnit.MILLISECONDS);
            return object;
        }
        return null;
    }

    /**
     * 续期时间
     * @return
     */
//    @Override
    public Integer genEmptyCacheTimeout() {
        return 60 + new Random().nextInt(30);
    }

//    @Override
    public Integer getElementCacheTimeout() {
        return PRODUCT_CACHE_TIMEOUT + new Random().nextInt(5) * 60 * 60;
    }

//    @Override
    public RLock redissonWriteLock(String key){
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
        RLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        return writeLock;
    }

//    @Override
    public RLock redissonReadLock(String key){
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
        RLock readLock = readWriteLock.readLock();
        readLock.lock();
        return readLock;
    }

//    @Override
    public void redissonReadWriteUnLock(RLock rLock){
        rLock.unlock();
    }


}
