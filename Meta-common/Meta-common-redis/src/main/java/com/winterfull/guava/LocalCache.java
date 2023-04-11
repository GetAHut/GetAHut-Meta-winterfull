package com.winterfull.guava;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;


/**
 * @author : ytxu5
 * @date: 2023/4/10
 */
@Slf4j
@Component
@Scope(value = "prototype")
public class LocalCache<K, V> {

    public LoadingCache<K, V> guavaCache(){
        return this.guavaCache(new LocalCacheConfig<K, V>());
    }

    @SuppressWarnings("unchecked")
    public LoadingCache<K, V> guavaCache(LocalCacheConfig<K, V> config){
        return  CacheBuilder.newBuilder()
                .concurrencyLevel(config.getConcurrencyLevel())
                .expireAfterWrite(config.getExpire(), config.getUnit())
                .initialCapacity(config.getInitialCapacity())
                .maximumSize(config.getMaximumSize())
                .removalListener(config.getListener())
                .build(config.getLoader());
    }

    public static void main(String[] args) throws ExecutionException {
        LocalCache<Integer, String> localCache = new LocalCache<>();
        LoadingCache<Integer, String> cache = localCache.guavaCache();
        cache.put(1, "12");
        System.out.println("1: " + cache.get(1));
        cache.put(2, "3");
        System.out.println("2: " + cache.get(1));
        cache.put(1, "3");
        System.out.println("1: " + cache.get(1));

    }

}
