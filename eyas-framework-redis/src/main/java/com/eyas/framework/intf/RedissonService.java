package com.eyas.framework.intf;

import com.eyas.framework.constraint.RedisKeyEnumConstraintInterface;

import java.util.concurrent.TimeUnit;

public interface RedissonService {
    /**
     * 获取key数据
     *
     * @param redisKeyEnumInterface key枚举
     * @return key的值
     */
    String getStr(RedisKeyEnumConstraintInterface redisKeyEnumInterface);

    /**
     * 设置key的值
     *
     * @param redisKeyEnumInterface key枚举
     * @param value key的值
     */
    void setStr(RedisKeyEnumConstraintInterface redisKeyEnumInterface, String value);

    /**
     * 设置带失效时间的key
     *
     * @param redisKeyEnumInterface key枚举
     * @param value key的值
     * @param timeToLive 失效时间
     * @param timeUnit 时间单位
     */
    void setStrTime(RedisKeyEnumConstraintInterface redisKeyEnumInterface, String value, Long timeToLive, TimeUnit timeUnit);

    boolean redissonTryLock(String key, long waitTime, TimeUnit timeUnit);

    void redissonUnLock(String key);
}
