package com.yzxie.study.eshopqueue.cache;

import com.yzxie.study.eshopcommon.constant.RedisConst;
import com.yzxie.study.eshopcommon.dto.OrderStatus;
import com.yzxie.study.eshopcommon.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-30
 * Description:
 **/
@Component
public class RedisCache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 递减库存数量
     * @param productId
     * @throws CacheException
     */
    public void decrProductQuantity(long productId) {
        BoundValueOperations valueOperations = redisTemplate.boundValueOps(RedisConst.SECKILL_NUMBER_KEY_PREFIX + productId);
        valueOperations.decrement();
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
