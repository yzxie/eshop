package com.yzxie.study.eshopbiz.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.yzxie.study.eshopcommon.constant.OrderConst.ORDER_QUEUE;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE);
    }
}
