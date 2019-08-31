package com.yzxie.study.eshopapi.controller.param;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Data
public class OrderRequest implements Serializable {
    private long productId;
    private double price;
    private int num;
    private String userId;
}
