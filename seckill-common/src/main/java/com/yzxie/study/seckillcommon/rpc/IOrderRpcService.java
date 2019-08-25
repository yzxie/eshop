package com.yzxie.study.seckillcommon.rpc;

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
}
