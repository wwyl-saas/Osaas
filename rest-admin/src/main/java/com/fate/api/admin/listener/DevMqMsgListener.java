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
 * @description: 消息监听器
 * @author: chenyixin
 * @create: 2019-07-25 19:48
 **/
@Profile("dev")
@Slf4j
@Component
public class DevMqMsgListener {
    @Autowired
    MqListener listener;


    @RabbitListener(
            bindings = {@QueueBinding(
                    exchange = @Exchange(value = MqConst.DEV_CUSTOMER_EXCHANGER, ignoreDeclarationExceptions = "true"),
                    value = @Queue(value = MqConst.DEV_ADMIN_QUEUE, durable = "true"),
                    key = "admin"
            ),@QueueBinding(
                    exchange = @Exchange(value = MqConst.DEV_MERCHANT_EXCHANGER, ignoreDeclarationExceptions = "true"),
                    value = @Queue(value = MqConst.DEV_ADMIN_QUEUE, durable = "true"),
                    key = "admin"
            )}
    )
    @RabbitHandler
    public void onDevMessage(MyMessage message, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        listener.process(message,channel,headers);
    }


}
