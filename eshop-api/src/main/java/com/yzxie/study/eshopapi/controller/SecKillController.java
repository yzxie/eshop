package com.yzxie.study.eshopapi.controller;

import com.yzxie.study.eshopapi.controller.param.OrderRequest;
import com.yzxie.study.eshopapi.controller.param.OrderStatusRequest;
import com.yzxie.study.eshopapi.limit.FlowLimit;
import com.yzxie.study.eshopapi.service.SeckillService;
import com.yzxie.study.eshopcommon.dto.OrderResult;
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
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/create")
    @ResponseBody
    @FlowLimit
    public ApiResponse create(@RequestBody OrderRequest orderRequest) {
        OrderResult orderResult = seckillService.createOrder(orderRequest.getProductId(), orderRequest.getNum(),
                orderRequest.getPrice(), orderRequest.getUserId());
        return ApiResponse.success(orderResult);
    }

    @RequestMapping("/check")
    @ResponseBody
    public ApiResponse checkOrder(@RequestBody OrderStatusRequest orderStatusRequest) {
        OrderResult orderResult = seckillService.checkOrderStatus(orderStatusRequest.getUserId(), orderStatusRequest.getOrderUuid());
        return ApiResponse.success(orderResult);
    }
}
