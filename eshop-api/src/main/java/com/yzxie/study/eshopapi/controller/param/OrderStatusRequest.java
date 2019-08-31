package com.yzxie.study.eshopapi.controller.param;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-31
 * Description:
 **/
@Data
public class OrderStatusRequest implements Serializable {
    private String orderUuid;
    private String userId;
}
