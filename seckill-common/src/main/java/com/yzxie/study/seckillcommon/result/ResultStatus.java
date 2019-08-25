package com.yzxie.study.seckillcommon.result;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public enum ResultStatus {
    /**
     * 成功
     */
    SUCCESS("200"),
    /**
     * 服务端异常
     */
    SERVER_ERROR("500"),
    /**
     * 权限不足
     */
    PERMISSION_DENY("403");

    private String value;

    public String getValue() {
        return value;
    }

    ResultStatus(String value) {
        this.value = value;
    }
}
