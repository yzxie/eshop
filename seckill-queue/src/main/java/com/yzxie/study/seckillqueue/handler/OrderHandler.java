package com.yzxie.study.seckillqueue.handler;

import com.alibaba.fastjson.JSON;
import com.yzxie.study.seckillcommon.bo.Order;
import com.yzxie.study.seckillqueue.repository.OrderDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Component
public class OrderHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private OrderDAO orderDAO;


    public void createOrder(Order order) {
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    // 插入记录数据库

                    // 更新缓存和数据库的库存数量

                    // 设置下单结果

                }
            });

        } catch (Exception e) {
            logger.error("createOrder {}", JSON.toJSONString(order), e);
        }
    }
}
