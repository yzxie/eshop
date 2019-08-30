package com.yzxie.study.eshopqueue.queue;

import com.yzxie.study.eshopcommon.dto.OrderDTO;
import com.yzxie.study.eshopqueue.handler.OrderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.yzxie.study.eshopcommon.constant.OrderConst.ORDER_QUEUE;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Component
@RabbitListener(queues = ORDER_QUEUE)
public class RabbitMqConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumer.class);

    @Autowired
    private OrderHandler orderHandler;

    @RabbitHandler
    public void process(OrderDTO orderDTO) {
        orderHandler.createOrder(orderDTO);
        logger.info("order {}", orderDTO.getUuid());
    }
}
