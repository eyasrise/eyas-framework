package com.eyas.framework.aspect.aop;

import com.eyas.framework.data.EyasFrameworkResult;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.intf.RedisService;
import com.eyas.framework.intf.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author eyasrise
 */
@Aspect
@Component
@Slf4j
public class ServiceLockAopConfig {

    @Autowired
    private RedissonService redissonService;

    @Value("${spring.application.name}")
    private String applicationName;


    @Pointcut("execution(public * *(..)) && @annotation(com.eyas.framework.aspect.config.ServiceLockService)" )
    public void addAdvice(){}


    @Around("addAdvice()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String logStr = "base-framework-ServiceLockAopConfig:";
        long start = System.currentTimeMillis();
        // 设置锁
        // 类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //方法名
        String methodName = joinPoint.getSignature().getName();
        String key = applicationName+ "_" + className + "_" + methodName;
        boolean lockKey = false;
        Object result;
        int count = 0;
        try {
            // 加锁开始
            // 加锁的实效是时间是一分钟持锁线程必须在一分钟执行完(锁时效60s)，其他线程2s自旋等待
            log.info(logStr +"redisKey:{}", key);
            while (!lockKey) {
                lockKey = this.redissonService.redissonTryLock(key, 2L, TimeUnit.SECONDS);
                // 自旋
                if (!lockKey) {
//                    log.info(Thread.currentThread().getName() + "线程--->没有获取到锁了！");
                    count++;
                    // 自旋锁-cpu核心次数
                    if (count >= Runtime.getRuntime().availableProcessors()) {
//                        log.info(Thread.currentThread().getName() + "线程--->超过了核心数，拒绝！");
                        break;
                    }
                }
            }
            if (!lockKey){
                // 自旋一定的次数如果还未获取到锁，那么我就释放锁，返回空对象
                // 防止自旋异常，增加一次获取锁尝试
                lockKey = this.redissonService.redissonTryLock(key, 2L, TimeUnit.SECONDS);
                // 依然失败就返回结束
                if (!lockKey) {
//                    log.info(Thread.currentThread().getName() + "线程--->获取锁失败！");
                    return EyasFrameworkResult.fail(ErrorFrameworkCodeEnum.SYSTEM_ERROR.getErrCode(), "获取锁失败");
                }
            }
//            log.info(logStr +"lockKey:{}", lockKey);
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            log.error("加锁失败!:{}", e.getMessage());
            return EyasFrameworkResult.fail(ErrorFrameworkCodeEnum.SYSTEM_ERROR.getErrCode(), "加锁失败");
        }finally {
            // 只有持锁线程才可以释放锁
            if (lockKey){
                this.redissonService.redissonUnLock(key);
                log.info(key + "锁已被释放");
            }
            long end = System.currentTimeMillis();
            log.info(logStr + Thread.currentThread().getName() + "线程--->" + "runTime--" + joinPoint + "\tUse time : " + (end - start) + " ms!");
        }
    }
}
