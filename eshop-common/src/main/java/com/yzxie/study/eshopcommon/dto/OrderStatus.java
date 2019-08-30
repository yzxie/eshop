package com.yzxie.study.eshopcommon.dto;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public enum OrderStatus {
    /**
     * 下单中，需要前端轮训状态
     */
    PENDING(1000),
    /**
     * 下单成功
     */
    SUCCESS(1001),
    /**
     * 下单失败
     */
    FAILURE(1002),
    /**
     * 不存在
     */
    NOT_FOUND(1003);

    private int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
