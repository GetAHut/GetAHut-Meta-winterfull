package com.winterfull.multicache;

import com.google.common.cache.LoadingCache;
import com.winterfull.guava.LocalCache;
import com.winterfull.redis.service.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 *
 * 多级缓存
 *      - 本地缓存 guava
 *      - 插件缓存 redis
 *
 * @author : ytxu5
 * @date: 2023/4/10
 */
@Component
public class MultiLevelCache<T> {

    @Resource
    private RedisService redisService;

    @Resource
    private LocalCache<String, T> localCache;

    @PostConstruct
    public void init(){
        LoadingCache<String, T> cache = localCache.guavaCache();
    }
}
