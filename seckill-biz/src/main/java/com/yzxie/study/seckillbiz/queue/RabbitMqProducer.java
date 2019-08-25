package com.yzxie.study.seckillbiz.queue;

import com.yzxie.study.seckillcommon.bo.Order;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.yzxie.study.seckillcommon.constant.OrderConst.ORDER_QUEUE;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Component
public class RabbitMqProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(Order order) {
        amqpTemplate.convertAndSend(ORDER_QUEUE, order);
    }
}
