package com.yzxie.study.eshopapi.service;

import com.yzxie.study.eshopcommon.dto.OrderResult;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public interface SeckillService {

    /**
     * 下单
     * @param productId
     * @param num
     * @param price
     * @param uuid
     * @return
     */
    OrderResult createOrder(long productId, int num, double price, String uuid);

    /**
     * 检查订单的状态
     * @param userId
     * @param orderUuId
     * @return
     */
    OrderResult checkOrderStatus(String userId, String orderUuId);
}
