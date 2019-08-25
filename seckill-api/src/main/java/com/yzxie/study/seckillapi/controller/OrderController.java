package com.yzxie.study.seckillapi.controller;

import com.yzxie.study.seckillapi.controller.param.OrderRequest;
import com.yzxie.study.seckillapi.dto.OrderResult;
import com.yzxie.study.seckillapi.service.OrderService;
import com.yzxie.study.seckillcommon.bo.OrderStatus;
import com.yzxie.study.seckillcommon.result.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/create")
    @ResponseBody
    public ApiResponse create(@RequestBody OrderRequest orderRequest) {
        OrderResult orderResult = orderService.createOrder(orderRequest.getProductId(), orderRequest.getUuid());
        return ApiResponse.success(orderRequest);
    }

    @RequestMapping("/check")
    @ResponseBody
    public ApiResponse checkOrder(String orderId) {
        // todo
        OrderResult orderResult = new OrderResult(orderId, OrderStatus.SUCCESS);
        return ApiResponse.success(orderResult);
    }
}
