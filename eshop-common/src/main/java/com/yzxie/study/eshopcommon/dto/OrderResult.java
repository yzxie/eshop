package com.yzxie.study.eshopcommon.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Data
public class OrderResult implements Serializable {
    private int orderStatus;
    private String orderUuId;

    public OrderResult(String orderUuId, int orderStatus) {
        this.orderUuId = orderUuId;
        this.orderStatus = orderStatus;
    }
}
