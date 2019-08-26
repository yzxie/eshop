package com.yzxie.study.seckillbiz.repository;

import com.yzxie.study.seckillcommon.bo.OrderStatus;
import com.yzxie.study.seckillcommon.constant.RedisConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.Collections;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-27
 * Description:
 **/
@Repository
public class SecKillRedisDAO {
    private static final Logger logger = LoggerFactory.getLogger(SecKillRedisDAO.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String DESC_LUA_SCRIPT = " local leftvalue = redis.call('get', KEYS[1]); "
            + " if ARGV[1] - leftvalue > 0 then return nil; else "
            + " return redis.call('decrby', KEYS[1], ARGV[1]); end; ";

    /**
     * 使用Lua脚本来实现原子递减
     * @param key redis的key
     * @param value 需要递减的值
     * @return 大于0，则说明还存在库存
     */
    public long descValueWithLua(String key, long value) {
        if (value <= 0)
            return -1;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(DESC_LUA_SCRIPT);
        redisScript.setResultType(Long.class);
        Long leftValue = redisTemplate.execute(redisScript, Collections.singletonList(key), "" + value);
        if (leftValue == null)
            return -1;
        return leftValue;
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
            logger.error("setSeckillResult {} {} {}", productId, uuid, orderStatus.getStatus());
        }
    }
}
