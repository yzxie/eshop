package com.yzxie.study.eshopapi.service;

import com.yzxie.study.eshopapi.dto.OrderResult;

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

    /**
     * 检查订单的状态
     * @param productId
     * @param orderUuId
     * @return
     */
    OrderResult checkOrderStatus(long productId, String orderUuId);
}
