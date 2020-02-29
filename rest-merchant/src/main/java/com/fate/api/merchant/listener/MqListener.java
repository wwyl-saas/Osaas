package com.fate.api.merchant.listener;

import com.fate.api.merchant.listener.processor.*;
import com.fate.api.merchant.service.MerchantService;
import com.fate.common.entity.Merchant;
import com.fate.common.enums.MessageTag;
import com.fate.common.model.MyMessage;
import com.fate.common.util.CurrentMerchantUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-24 02:25
 **/
@Slf4j
@Component
public class MqListener {
    @Autowired
    AppointmentCancelProcessor appointmentCancelProcessor;
    @Autowired
    AppointmentCreateProcessor appointmentCreateProcessor;
    @Autowired
    ChargeSuccessProcessor chargeSuccessProcessor;
    @Autowired
    FeedbackeCreateProcessor feedbackeCreateProcessor;
    @Autowired
    OrderCancelProcessor orderCancelProcessor;
    @Autowired
    OrderSettleProcessor orderSettleProcessor;

    @Autowired
    MerchantService merchantService;

    /**
     * 为区分环境
     * @param message
     * @param channel
     * @param headers
     * @throws IOException
     */
    public void process(MyMessage message, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        Boolean ack=true;//不成功
        try {
            Long merchantId= (Long) headers.get("merchantId");
            Merchant merchant=merchantService.getById(merchantId);
            Assert.notNull(merchant,"应用商户不存在");
            CurrentMerchantUtil.addMerchant(merchant);
            Optional<MessageTag> messageTag = MessageTag.getEnum(headers.get("messageTag").toString()) ;
            MessageProcessor<MyMessage> messageProcessor=null;
            switch (messageTag.get()){
                case APPOINTMENT_CREATE:
                    messageProcessor=appointmentCreateProcessor;
                    break;
                case CUSTOMER_APPOINTMENT_CANCEL:
                    messageProcessor=appointmentCancelProcessor;
                    break;
                case CHARGE_SUCCESS:
                    messageProcessor=chargeSuccessProcessor;
                    break;
                case FEEDBACK_CREATE:
                    messageProcessor=feedbackeCreateProcessor;
                    break;
                case ORDER_SETTLE:
                    messageProcessor=orderSettleProcessor;
                    break;
                case CUSTOMER_ORDER_CANCEL:
                    messageProcessor=orderCancelProcessor;
                    break;
            }
            ack=messageProcessor.handle(message);
        }catch (Exception e){
            log.error("消费信息失败:{}",message);
        }
        log.info("收到消息:{},消费结果:{}" , message,!ack);
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, ack);
    }

}
