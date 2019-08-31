package com.yzxie.study.eshopapi.service.impl;

import com.yzxie.study.eshopapi.service.OrderService;
import com.yzxie.study.eshopcommon.dto.OrderResult;
import com.yzxie.study.eshopcommon.dto.OrderStatus;
import com.yzxie.study.eshopcommon.exception.ApiException;
import com.yzxie.study.eshopcommon.rpc.IOrderRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private IOrderRpcService orderRpcService;

    @Override
    public OrderResult createOrder(long productId, int num, double price, String uuid) {
        try {
            // RPC调用发送到队列
            OrderResult orderResult = orderRpcService.sendOrderToMq(productId, num, price, uuid);
            return orderResult;
        } catch (Exception e) {
            throw new ApiException("服务异常，请稍后再试");
        }
    }

    @Override
    public OrderResult checkOrderStatus(String userId, String orderUuId) {
        try {
            // RPC调用发送到队列
            int orderStatus = orderRpcService.getOrderStatus(userId, orderUuId);
            OrderResult result = new OrderResult(orderUuId, orderStatus);
            return result;
        } catch (Exception e) {
            logger.error("checkOrderStatus {} {}", userId, orderUuId, e);
            throw new ApiException("服务异常，请稍后再试");
        }
    }
}
