package com.yzxie.study.eshopcommon.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-31
 * Description:
 **/
@Data
public class OrderItemDTO implements Serializable {
    private long productId;
    private int num;
    private double price;
}
