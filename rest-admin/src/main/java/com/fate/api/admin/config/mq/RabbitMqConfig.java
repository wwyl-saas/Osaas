package com.fate.api.admin.config.mq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: parent
 * @description: RabbitmqConfig RabbitTemplate只允许设置一个callback方法，你可以将RabbitTemplate的bean设为单例然后设置回调
 * @author: chenyixin
 * @create: 2019-09-23 13:14
 **/
@Configuration
@EnableRabbit
public class RabbitMqConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public RabbitCallbackTemplate rabbitCallbackTemplate(){
        return new RabbitCallbackTemplate(rabbitTemplate);
    }
}
