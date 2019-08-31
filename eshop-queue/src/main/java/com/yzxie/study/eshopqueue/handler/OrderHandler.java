package com.yzxie.study.eshopqueue.handler;

import com.alibaba.fastjson.JSON;
import com.yzxie.study.eshopcommon.bo.OrderBO;
import com.yzxie.study.eshopcommon.bo.OrderItemBO;
import com.yzxie.study.eshopcommon.dto.OrderDTO;
import com.yzxie.study.eshopcommon.dto.OrderItemDTO;
import com.yzxie.study.eshopcommon.dto.OrderStatus;
import com.yzxie.study.eshopqueue.cache.RedisCache;
import com.yzxie.study.eshopqueue.repository.OrderDAO;
import com.yzxie.study.eshopqueue.repository.OrderItemDAO;
import com.yzxie.study.eshopqueue.repository.ProductQuantityDAO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
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
            Pair<OrderBO, List<OrderItemBO>> orderBOListPair = buildOrderBO(orderDTO);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    // 生成订单记录
                    orderDAO.insert(orderBOListPair.getLeft());
                    List<OrderItemBO> orderItemBOList = orderBOListPair.getRight();
                    orderItemDAO.bulkInsert(orderItemBOList);
                    // 更新数据库的库存数量
                    for (OrderItemBO orderItemBO : orderItemBOList) {
                        productQuantityDAO.decrQuantity(orderItemBO.getNum());
                    }
                    // 设置下单结果
                    redisCache.setSeckillResult(orderDTO.getUserId(), orderDTO.getUuid(), OrderStatus.SUCCESS);
                }
            });
        } catch (Exception e) {
            logger.error("createOrder {}", JSON.toJSONString(orderDTO), e);
        }
    }

    private Pair<OrderBO, List<OrderItemBO>> buildOrderBO(OrderDTO orderDTO) {
        OrderBO orderBO = new OrderBO();
        orderBO.setUserId(orderDTO.getUserId());
        orderBO.setUuid(orderDTO.getUuid());
        List<OrderItemDTO> orderItemDTOList = orderDTO.getOrderItemDTOList();
        List<OrderItemBO> orderItemBOList = new ArrayList<>(orderItemDTOList.size());
        double cost = 0.0;
        for (OrderItemDTO orderItemDTO : orderItemDTOList) {
            OrderItemBO orderItemBO = new OrderItemBO();
            orderItemBO.setProductId(orderItemDTO.getProductId());
            orderItemBO.setOrderUuid(orderDTO.getUuid());
            // 计算总金额
            cost += (orderItemDTO.getNum() * orderItemDTO.getPrice());
            orderItemBO.setNum(orderItemDTO.getNum());
            orderItemBO.setPrice(orderItemDTO.getPrice());
            orderItemBO.setCreateTime(new Date());
            orderItemBOList.add(orderItemBO);
        }
        orderBO.setCost(cost);
        orderBO.setCreateTime(new Date());
        return Pair.of(orderBO, orderItemBOList);
    }
}
