package com.eyas.framework.impl;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 本地缓存工具类
 *
 * @author eyas
 */
@Component
public class CacheUtils {

    public final Cache<String, Object> caffeineCache;

    public CacheUtils(Cache<String, Object> caffeineCache) {
        this.caffeineCache = caffeineCache;
    }

    /**
     * 添加或更新缓存
     *
     * @param key 缓存key
     * @param value 缓存值
     */
    public void putAndUpdateCache(String key, Object value) {
        caffeineCache.put(key, value);
    }


    /**
     * 获取对象缓存
     *
     * @param key 缓存key
     * @return 缓存数据
     */
    public <T> T getObjCacheByKey(String key, Class<T> t) {
        caffeineCache.getIfPresent(key);
        return (T) caffeineCache.asMap().get(key);
    }

    /**
     * 根据key删除缓存
     *
     * @param key 删除key
     */
    public void removeCacheByKey(String key) {
        // 从缓存中删除
        caffeineCache.asMap().remove(key);
    }
}
