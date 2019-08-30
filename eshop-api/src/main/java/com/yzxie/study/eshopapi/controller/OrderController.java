package com.yzxie.study.eshopapi.controller;

import com.yzxie.study.eshopapi.controller.param.OrderRequest;
import com.yzxie.study.eshopapi.controller.param.OrderStatusRequest;
import com.yzxie.study.eshopapi.dto.OrderResult;
import com.yzxie.study.eshopapi.service.OrderService;
import com.yzxie.study.eshopcommon.result.ApiResponse;
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
        return ApiResponse.success(orderResult);
    }

    @RequestMapping("/check")
    @ResponseBody
    public ApiResponse checkOrder(@RequestBody OrderStatusRequest orderStatusRequest) {
        OrderResult orderResult = orderService.checkOrderStatus(orderStatusRequest.getProductId(), orderStatusRequest.getOrderUuid());
        return ApiResponse.success(orderResult);
    }
}
