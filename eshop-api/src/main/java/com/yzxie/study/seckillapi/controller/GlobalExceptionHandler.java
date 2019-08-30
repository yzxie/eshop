package com.yzxie.study.seckillapi.controller;

import com.yzxie.study.seckillcommon.exception.ApiException;
import com.yzxie.study.seckillcommon.result.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ApiException.class)
    public ApiResponse handleApiException(ApiException e) {
        logger.error("handleApiException", e);
        return ApiResponse.error(e.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResponse handleException(Exception e) {
        logger.error("handleException", e);
        return ApiResponse.error("服务端异常");
    }
}
