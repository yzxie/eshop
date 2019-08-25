package com.yzxie.study.seckillbiz.service;

import com.yzxie.study.seckillbiz.queue.RabbitMqProducer;
import com.yzxie.study.seckillcommon.bo.Order;
import com.yzxie.study.seckillcommon.rpc.IOrderRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Service
public class OrderRpcServiceImpl implements IOrderRpcService {

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    /**
     * 秒杀订单处理线程池
     */
    private ExecutorService createOrderThreadPool = new ThreadPoolExecutor(10, 100,
            0L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>(1000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("create-order-thread");
            return thread;
        }
    });


    @Override
    public String sendOrderToMq(long productId, String uuid) {
        Order order = new Order();
        order.setProductId(productId);
        order.setUuid(uuid);
        String orderId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        order.setId(orderId);
        // 发送订单到队列，实现流量削峰和异步处理
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // todo 检查是否还有库存，如果有则发送到队列
                rabbitMqProducer.send(order);
            }
        };
        createOrderThreadPool.execute(task);

        return orderId;
    }
}
