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
public class OrderBO {
    private long id;
    private String uuid;
    private String userId;
    private double cost;
    private Date createTime;
}
