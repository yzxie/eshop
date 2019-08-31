package com.yzxie.study.eshopcommon.bo;

import lombok.Data;

import java.util.Date;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-30
 * Description:
 **/
@Data
public class OrderItemBO {
    private String orderUuid;
    private long productId;
    private double price;
    private int num;
    private Date createTime;
}
