package com.yzxie.study.seckillapi.service.impl;

import com.yzxie.study.seckillapi.dto.OrderResult;
import com.yzxie.study.seckillapi.service.OrderService;
import com.yzxie.study.seckillcommon.bo.OrderStatus;
import com.yzxie.study.seckillcommon.exception.ApiException;
import com.yzxie.study.seckillcommon.rpc.IOrderRpcService;
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

    @Autowired
    private IOrderRpcService orderRpcService;

    @Override
    public OrderResult createOrder(long productId, String uuid) {
        try {
            // RPC调用发送到队列
            String orderId = orderRpcService.sendOrderToMq(productId, uuid);
            OrderResult result = new OrderResult(orderId, OrderStatus.PENDING);
            return result;
        } catch (Exception e) {
            throw new ApiException("服务异常，请稍后再试");
        }
    }
}
