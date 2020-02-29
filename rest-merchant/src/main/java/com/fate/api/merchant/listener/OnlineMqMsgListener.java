package com.fate.api.merchant.listener;

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
 * @create: 2019-09-24 02:27
 **/
@Profile("online")
@Slf4j
@Component
public class OnlineMqMsgListener {
    @Autowired
    MqListener mqListener;


    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(value = MqConst.ONLINE_CUSTOMER_EXCHANGER, ignoreDeclarationExceptions = "true"),
                    value = @Queue(value = MqConst.ONLINE_MERCHANT_QUEUE, durable="true"),
                    key = "merchant"
            )
    )
    @RabbitHandler
    public void onOnlineMessage(MyMessage message, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        mqListener.process(message,channel,headers);
    }
}
