package com.yzxie.study.eshopcommon.rpc;

import com.yzxie.study.eshopcommon.dto.OrderStatus;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
public interface IOrderRpcService {

    /**
     * 发送订单到队列
     * @param productId
     * @param uuid
     * @return 订单id
     */
    String sendOrderToMq(long productId, String uuid);

    /**
     * 检查订单的状态
     * @param productId
     * @param orderUuid
     * @return
     */
    int getOrderStatus(long productId, String orderUuid);
}
