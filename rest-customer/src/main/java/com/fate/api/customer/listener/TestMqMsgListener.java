package com.fate.api.customer.listener;

import com.fate.common.cons.MqConst;
import com.fate.common.model.MyMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-24 02:22
 **/
@Profile("test")
@Slf4j
@Component
public class TestMqMsgListener {
    @Autowired
    MqListener mqListener;


    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(value = MqConst.TEST_MERCHANT_EXCHANGER, ignoreDeclarationExceptions = "true"),
                    value = @Queue(value = MqConst.TEST_CUSTOMER_QUEUE, durable="true"),
                    key = "customer"
            )
    )
    @RabbitHandler
    public void onTestMessage(MyMessage message, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        mqListener.process(message,channel,headers);
    }

}
