package com.eyas.framework.aspect.config;

import java.lang.annotation.*;

/**
 * @author eyasrise
 * 分布式锁切面注解，redisKey:项目名称+类名+方法名(注意不要重复)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceLockAllowOneService {
}
