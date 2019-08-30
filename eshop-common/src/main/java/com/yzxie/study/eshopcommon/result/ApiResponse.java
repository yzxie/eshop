package com.yzxie.study.eshopcommon.result;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public class ApiResponse {

    private String status;

    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ApiResponse success(Object data) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResultStatus.SUCCESS.getValue());
        apiResponse.setData(data);
        return apiResponse;
    }

    public static ApiResponse error(Object data) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResultStatus.SERVER_ERROR.getValue());
        apiResponse.setData(data);
        return apiResponse;
    }
}
