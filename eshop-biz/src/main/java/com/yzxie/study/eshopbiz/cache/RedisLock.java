package com.yzxie.study.eshopbiz.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-29
 * Description:
 **/
@Component
public class RedisLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    /**
     * redis分布式锁的值，实现解锁由加锁的线程来实现，使用本地缓存来减少对Redis的查询
     */
    private static final Map<String, String> LOCK_VALUE_MAP = new ConcurrentHashMap<>();

    /**
     * 秒杀分布式锁对应的键key的前戳
     */
    public static final String SECKILL_LOCK_PREFIX = "seckill_redis_lock:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 加锁
     * @param lockKey
     * @param lockValue
     * @param expireSeconds
     * @return
     */
    public boolean tryLock(String lockKey, String lockValue, long expireSeconds) {
        try {
            BoundValueOperations<String, Object> valueOperations = redisTemplate.boundValueOps(lockKey);
            // Redis的setnx命名
            // 利用Redis的单线程特性
            boolean success = valueOperations.setIfAbsent(lockValue);
            if (success) {
                // 设置超时时间，避免死锁
                valueOperations.expire(expireSeconds, TimeUnit.SECONDS);
                LOCK_VALUE_MAP.put(lockKey, lockValue);
            }
            return success;
        } catch (Exception e) {
            logger.error("tryLock {} {} {}", lockKey, lockValue, expireSeconds, e);
        }
        return false;
    }

    /**
     * 解锁
     * @param lockKey
     * @return
     */
    public boolean release(String lockKey, String lockValue) {
        try {
            String value = LOCK_VALUE_MAP.get(lockKey);
            if (lockValue.equals(value)) {
                return redisTemplate.delete(lockKey);
            }
        } catch (Exception e) {
            logger.error("release {} {}", lockKey, lockValue, e);
        }
        return false;
    }
}
