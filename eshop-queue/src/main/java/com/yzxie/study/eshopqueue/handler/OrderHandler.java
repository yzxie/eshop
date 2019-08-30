package com.yzxie.study.eshopqueue.handler;

import com.alibaba.fastjson.JSON;
import com.yzxie.study.eshopcommon.bo.OrderBO;
import com.yzxie.study.eshopcommon.bo.OrderItemBO;
import com.yzxie.study.eshopcommon.dto.OrderDTO;
import com.yzxie.study.eshopcommon.dto.OrderStatus;
import com.yzxie.study.eshopqueue.cache.RedisCache;
import com.yzxie.study.eshopqueue.repository.OrderDAO;
import com.yzxie.study.eshopqueue.repository.OrderItemDAO;
import com.yzxie.study.eshopqueue.repository.ProductQuantityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

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
    private RedisCache redisCache;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private ProductQuantityDAO productQuantityDAO;

    public void createOrder(OrderDTO orderDTO) {
        try {
            OrderBO orderBO = new OrderBO();
            List<OrderItemBO> orderItemBOList = new ArrayList<>(orderDTO.getOrderItemDTOList().size());

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    // 生成订单记录
                    orderDAO.insert(orderBO);
                    orderItemDAO.bulkInsert(orderItemBOList);
                    // 更新数据库的库存数量
                    productQuantityDAO.decrQuantity(orderDTO.getProductId());
                    // 更新缓存的库存数量
                    redisCache.decrProductQuantity(orderDTO.getProductId());
                    // 设置下单结果
                    redisCache.setSeckillResult(orderDTO.getProductId(), orderDTO.getUuid(), OrderStatus.SUCCESS);
                }
            });
        } catch (Exception e) {
            logger.error("createOrder {}", JSON.toJSONString(orderDTO), e);
        }
    }
}
