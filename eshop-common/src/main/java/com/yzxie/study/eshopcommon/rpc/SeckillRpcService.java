package com.yzxie.study.eshopcommon.rpc;

import com.yzxie.study.eshopcommon.dto.OrderResult;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public interface SeckillRpcService {

    /**
     * 发送订单到队列
     * @param productId
     * @param num
     * @param price
     * @param uuid
     * @return 订单id
     */
    OrderResult sendOrderToMq(long productId, int num, double price, String uuid);

    /**
     * 检查订单的状态
     * @param userId
     * @param orderUuid
     * @return
     */
    int getOrderStatus(String userId, String orderUuid);
}
