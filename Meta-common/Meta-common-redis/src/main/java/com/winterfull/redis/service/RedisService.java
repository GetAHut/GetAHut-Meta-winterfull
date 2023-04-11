package com.winterfull.redis.service;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : ytxu5
 * @date: 2023/4/10
 */
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * set
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void setCacheObject(final String key, final T value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * set
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @param <T>
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit unit){
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 设置过期时间
     * @param key
     * @param timeout
     * @return
     */
    public boolean expire(final String key, final long timeout){
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean expire(final String key, final long timeout, final TimeUnit timeUnit){
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取过期时间
     * @param key
     * @return
     */
    public long getExpire(final String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 校验redis是否存在key
     * @param key
     * @return
     */
    public Boolean hasKey(final String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取缓存对象
     * @param key
     * @return
     * @param <T>
     */
    public <T> T getCacheObject(final String key){
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 删除某个key
     * @param key
     * @return
     */
    public boolean delete(final String key){
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     * @param collection
     * @return
     */
    public boolean delete(final Collection collection){
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * 存放整个list
     * @param key
     * @param list
     * @return
     * @param <T>
     */
    public <T> long setCacheList(final String key, final List<T> list){
        Long aLong = redisTemplate.opsForList().rightPushAll(key, list);
        return aLong == null ? 0 : aLong;
    }

    /**
     * 获取list
     * @param key
     * @return
     * @param <T>
     */
    public <T> List<T> getCacheList(final String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 存放整个set
     * @param key
     * @param data
     * @return
     * @param <T>
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> data){
        BoundSetOperations<String, T> operations = redisTemplate.boundSetOps(key);
        Iterator<T> iterator = data.iterator();
        while (iterator.hasNext()){
            operations.add(iterator.next());
        }
        return operations;
    }

    /**
     * 获取set
     * @param key redis key
     * @return
     * @param <T>
     */
    public <T> Set<T> getCacheSet(final String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 存放整个hash
     * @param key     redis key
     * @param dataMap hash data
     * @param <T>
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap){
        if (null != dataMap){
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获取整个hash
     * @param key redis key
     * @return
     * @param <T>
     */
    public <T> Map<String, T> getCacheMap(final String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 向hash中存放数据
     * @param key     redis key
     * @param hashKey hash key
     * @param value   hash value
     * @param <T>
     */
    public <T> void setCacheMapValue(final String key, final String hashKey, final T value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取hash中某个值
     * @param key     redis key
     * @param hashKey hash key
     * @return
     * @param <T>
     */
    public <T> T getCacheMapValue(final String key, final String hashKey){
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    /**
     * 删除hash中值
     * @param key      redis key
     * @param hashKey  hash key
     * @return
     */
    public boolean deleteCacheMapValue(final String key, final String hashKey){
        return redisTemplate.opsForHash().delete(key, hashKey) > 0;
    }

    /**
     * 获取缓存的对象列表
     * @param pattern 匹配格式
     * @return
     */
    public Collection<String> keys(final String pattern){
        return redisTemplate.keys(pattern);
    }




}
