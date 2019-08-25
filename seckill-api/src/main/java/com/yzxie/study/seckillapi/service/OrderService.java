package com.yzxie.study.seckillapi.service;

import com.yzxie.study.seckillapi.dto.OrderResult;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public interface OrderService {

    /**
     * 下单
     * @param productId
     * @param uuid
     * @return
     */
    OrderResult createOrder(long productId, String uuid);
}
