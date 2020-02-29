package com.fate.api.admin.listener;

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
 * @create: 2019-09-24 02:17
 **/

@Profile("test")
@Component
@Slf4j
public class TestMqMsgListener {
    @Autowired
    MqListener listener;


    @RabbitListener(
            bindings = {@QueueBinding(
                    exchange = @Exchange(value = MqConst.TEST_CUSTOMER_EXCHANGER, ignoreDeclarationExceptions = "true"),
                    value = @Queue(value = MqConst.TEST_ADMIN_QUEUE, durable = "true"),
                    key = "admin"
            ),@QueueBinding(
                    exchange = @Exchange(value = MqConst.TEST_MERCHANT_EXCHANGER, ignoreDeclarationExceptions = "true"),
                    value = @Queue(value = MqConst.TEST_ADMIN_QUEUE, durable = "true"),
                    key = "admin"
            )}
    )
    @RabbitHandler
    public void onTestMessage(MyMessage message, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        listener.process(message,channel,headers);
    }
}
