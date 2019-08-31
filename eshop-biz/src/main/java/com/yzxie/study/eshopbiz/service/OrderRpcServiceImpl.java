package com.yzxie.study.eshopbiz.service;

import com.yzxie.study.eshopbiz.cache.RedisCache;
import com.yzxie.study.eshopbiz.queue.RabbitMqProducer;
import com.yzxie.study.eshopcommon.constant.RedisConst;
import com.yzxie.study.eshopcommon.dto.OrderDTO;
import com.yzxie.study.eshopcommon.dto.OrderItemDTO;
import com.yzxie.study.eshopcommon.dto.OrderResult;
import com.yzxie.study.eshopcommon.dto.OrderStatus;
import com.yzxie.study.eshopcommon.rpc.IOrderRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Service
public class OrderRpcServiceImpl implements IOrderRpcService {
    private static final Logger logger = LoggerFactory.getLogger(IOrderRpcService.class);

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private RedisCache redisCache;

    @Override
    public OrderResult sendOrderToMq(long productId, int num, double price, String userId) {
        OrderDTO orderDTO = buildOrderDTO(productId, num, price, userId);
        // 发送订单到队列，实现流量削峰和异步处理
        // 检查是否还有库存，如果有则发送到队列
        long remainNum = redisCache.descValueWithLua(RedisConst.SECKILL_NUMBER_KEY_PREFIX + productId,
                num, productId);
        if (remainNum >= 0) {
            rabbitMqProducer.send(orderDTO);
            return new OrderResult(orderDTO.getUuid(), OrderStatus.PENDING.getStatus());
        } else {
            // 直接返回抢购失败
            return new OrderResult(orderDTO.getUuid(), OrderStatus.FAILURE.getStatus());
        }
    }

    private OrderDTO buildOrderDTO(long productId, int num, double price, String userId) {
        // order
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(userId);
        String orderUuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        orderDTO.setUuid(orderUuid);
        // orderItem
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setNum(num);
        orderItemDTO.setProductId(productId);
        orderItemDTO.setPrice(price);
        orderItemDTOList.add(orderItemDTO);
        orderDTO.setOrderItemDTOList(orderItemDTOList);
        return orderDTO;
    }

    @Override
    public int getOrderStatus(String userId, String orderUuid) {
        try {
            Integer status = redisCache.getSeckillResult(userId, orderUuid);
            if (status == null) {
                return OrderStatus.NOT_FOUND.getStatus();
            }
            return status;
        } catch (Exception e) {
            logger.error("getOrderStatus {}", orderUuid, e);
        }
        return -1;
    }
}
