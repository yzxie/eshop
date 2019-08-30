package com.yzxie.study.eshopbiz.service;

import com.yzxie.study.eshopbiz.cache.RedisCache;
import com.yzxie.study.eshopbiz.queue.RabbitMqProducer;
import com.yzxie.study.eshopcommon.bo.OrderItemBO;
import com.yzxie.study.eshopcommon.constant.RedisConst;
import com.yzxie.study.eshopcommon.dto.OrderDTO;
import com.yzxie.study.eshopcommon.dto.OrderItemDTO;
import com.yzxie.study.eshopcommon.dto.OrderStatus;
import com.yzxie.study.eshopcommon.rpc.IOrderRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private static final Logger logger = LoggerFactory.getLogger(IOrderRpcService.class);

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private RedisCache redisCache;

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
    public String sendOrderToMq(long productId, String userId) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductId(productId);
        orderDTO.setUserId(userId);
        String orderUuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        orderDTO.setUuid(orderUuid);
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        orderDTO.setOrderItemDTOList(orderItemDTOList);
        // 发送订单到队列，实现流量削峰和异步处理
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 检查是否还有库存，如果有则发送到队列
                long remainNum = redisCache.descValueWithLua(RedisConst.SECKILL_NUMBER_KEY_PREFIX + productId,
                        1, productId);
                if (remainNum > 0) {
                    rabbitMqProducer.send(orderDTO);
                } else {
                    // 直接返回抢购失败
                    redisCache.setSeckillResult(productId, orderUuid, OrderStatus.FAILURE);
                }
            }
        };
        createOrderThreadPool.execute(task);
        return orderUuid;
    }

    @Override
    public int getOrderStatus(long productId, String orderUuid) {
        try {
            Integer status = redisCache.getSeckillResult(productId, orderUuid);
            if (status == null) {
                return OrderStatus.NOT_FOUND.getStatus();
            }
            return status;
        } catch (Exception e) {
            logger.error("getOrderStatus {}", orderUuid, e);
        }
        return -1;
    }
}
