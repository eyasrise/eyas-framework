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
public class ServiceLockAllowOneAopConfig {

    @Autowired
    private RedissonService redissonService;

    @Value("${spring.application.name}")
    private String applicationName;


    @Pointcut("execution(public * *(..)) && @annotation(com.eyas.framework.aspect.config.ServiceLockAllowOneService)" )
    public void addAdvice(){}


    /**
     * 幂等锁，只允许一次线程访问资源
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("addAdvice()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String logStr = "base-framework-ServiceLockAllowOneAopConfig-";
        long start = System.currentTimeMillis();
        // 设置锁
        // 类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //方法名
        String methodName = joinPoint.getSignature().getName();
        String key = applicationName + "_" + className + "_" + methodName;
        boolean lockKey = false;
        Object result;
        try {
            // 加锁开始
            // 加锁的实效是时间是一分钟持锁线程必须在600s执行完(锁时效600s)，其他线程直接结束
            log.info(logStr +"redisKey:{}", key);
            while (!lockKey) {
                lockKey = this.redissonService.redissonTryLock(key, 2L, TimeUnit.SECONDS);
                // 自旋
                if (!lockKey) {
                    break;
                }
            }
            if (!lockKey){
                // 防止自旋异常，增加一次获取锁尝试
                lockKey = this.redissonService.redissonTryLock(key, 2L, TimeUnit.SECONDS);
                // 依然失败就返回结束
                if (!lockKey) {
                    return EyasFrameworkResult.fail(ErrorFrameworkCodeEnum.SYSTEM_ERROR.getErrCode(), "获取锁失败");
                }
            }
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
