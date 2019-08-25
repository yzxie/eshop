package com.yzxie.study.seckillapi.dto;

import com.yzxie.study.seckillcommon.bo.OrderStatus;

import java.io.Serializable;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public class OrderResult implements Serializable {
    private OrderStatus orderStatus;
    private String orderId;

    public OrderResult(String orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
