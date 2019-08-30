package com.yzxie.study.seckillbiz.cache;

import com.yzxie.study.seckillbiz.repository.SeckillNumDAO;
import com.yzxie.study.seckillcommon.bo.OrderStatus;
import com.yzxie.study.seckillcommon.constant.RedisConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

import static com.yzxie.study.seckillcommon.constant.RedisConst.SECKILL_NUMBER_KEY_PREFIX;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-27
 * Description:
 **/
@Component
public class RedisCache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SeckillNumDAO seckillNumDAO;

    @Autowired
    private RedisLock redisLock;

    /**
     * lua脚本，先获取指定产品的秒杀数量，再递减
     */
    private static final String DESC_LUA_SCRIPT = " local remain_num = redis.call('get', KEYS[1]); "
            + " if remain_num == nil then return nil; end; "
            + " if ARGV[1] - remain_num > 0 then return -1; else "
            + " return redis.call('decrby', KEYS[1], ARGV[1]); end; ";

    /**
     * 使用Lua脚本来实现原子递减
     * @param key redis的key
     * @param value 需要递减的值
     * @param productId
     * @return 大于0，则说明还存在库存
     */
    public long descValueWithLua(String key, long value, long productId) {
        if (value <= 0)
            return -1;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(DESC_LUA_SCRIPT);
        redisScript.setResultType(Long.class);
        Long remainNum = redisTemplate.execute(redisScript, Collections.singletonList(key), "" + value);

        // 递减失败，缓存不存在值
        if (remainNum == null) {
            // 加锁，避免缓存没有秒杀数量时，大量访问数据库
            synchronized (LOCK) {
                // double check
                remainNum = getSecKillNum(productId);
                if (remainNum == null) {
                    // 从数据库加载，如果数据库不存在，则返回-1
                    remainNum = seckillNumDAO.loadSeckillNum(productId);
                    if (remainNum == null) {
                        return -1;
                    }
                    // 分布式锁，避免不同机器实例的并发设值问题
                    final String lockKey = RedisLock.SECKILL_LOCK_PREFIX + productId;
                    final String lockValue = UUID.randomUUID().toString().replace("-", "");
                    try {
                        boolean lock = redisLock.tryLock(lockKey, lockValue, 10);
                        if (lock) {
                            // Redis单线程特性，
                            // 此次会在后面的"递减"之前执行，
                            // 如两个实例同时执行了tryLock方法，获取分布式锁失败的实例会执行到execute部分，不过也是在此操作之后的，
                            // 故可以读取到此处的设值
                            setSecKillNum(productId, remainNum);
                        }
                    } catch (Exception e) {
                        logger.error("redis try lock error {}", productId, e);
                    } finally {
                        redisLock.release(lockKey, lockValue);
                    }
                }

                // 递减
                remainNum = redisTemplate.execute(redisScript, Collections.singletonList(key), "" + value);
            }

        }
        return remainNum;
    }

    /**
     * 设置指定产品的秒杀数量
     * @param productId
     * @param num
     */
    public void setSecKillNum(long productId, long num) {
        try {
            BoundValueOperations<String, Object> valueOperations = redisTemplate.boundValueOps(SECKILL_NUMBER_KEY_PREFIX + productId);
            valueOperations.set(num);
        } catch (Exception e) {
            logger.error("setSecKillNum {} {]", productId, num, e);
        }
    }

    /**
     * 获取指定产品的秒杀数量
     * @param productId
     * @return
     */
    public Long getSecKillNum(long productId) {
        try {
            BoundValueOperations<String, Object> valueOperations = redisTemplate.boundValueOps(SECKILL_NUMBER_KEY_PREFIX + productId);
            Object value = valueOperations.get();
            return (Long)value;
        } catch (Exception e) {
            logger.error("getSecKillNum {}", productId, e);
        }
        return null;
    }

    /**
     * 设置抢购结果
     * @param productId
     * @param uuid
     * @param orderStatus
     */
    public void setSeckillResult(long productId, String uuid, OrderStatus orderStatus) {
        try {
            BoundHashOperations<String, String, Object> hashOperations =
                    redisTemplate.boundHashOps(RedisConst.SECKILL_RESULT_KEY_PREFIX + productId);
            hashOperations.put(uuid, orderStatus.getStatus());
        } catch (Exception e) {
            logger.error("setSeckillResult {} {} {}", productId, uuid, orderStatus.getStatus(), e);
        }
    }
}
