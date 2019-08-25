package com.yzxie.study.seckillcommon.bo;

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
    FAILURE(1002);

    private int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
