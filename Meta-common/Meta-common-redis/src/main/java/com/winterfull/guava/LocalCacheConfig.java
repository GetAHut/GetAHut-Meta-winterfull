package com.winterfull.guava;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : ytxu5
 * @date: 2023/4/10
 */
@Slf4j
@Data
@Accessors(chain = true)
public class LocalCacheConfig<K, V> {

    /** 并发数 */
    private Integer concurrencyLevel = 10;
    /** 过期时间 */
    private long expire = 24;
    /** 单位 */
    private TimeUnit unit = TimeUnit.HOURS;
    /** 初始容量 */
    private Integer initialCapacity = 16;
    /** 最大容量 */
    private Integer maximumSize = 1024;
    /** 缓存key移除监听器 */
    private RemovalListener<K, V> listener = removalNotification -> {
        log.debug("[localCache] key : {} was removed, cause : {}",
                removalNotification.getKey(), removalNotification.getCause());
    };
    /** 初始缓存加载器 */
    private CacheLoader<K, V> loader = new CacheLoader<K, V>() {
        @Override
        public V load(K k) throws Exception {
            // DB extend
            log.debug("[localCache] key : {}, init", k);
            return null;
        }
    };
}
