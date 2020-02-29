package com.fate.api.merchant.config.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @program: parent
 * @description: 一次性包装template
 * @author: chenyixin
 * @create: 2019-09-23 23:01
 **/
@Slf4j
public class RabbitCallbackTemplate implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback{
    private RabbitTemplate rabbitTemplate;

    public RabbitCallbackTemplate(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
        this.rabbitTemplate = rabbitTemplate;
    }

    public RabbitTemplate getRabbitTemplate(){
        return this.rabbitTemplate;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("correlationData: " + correlationData);
        log.info("ack: " + ack);
        if(!ack){
            log.info("异常处理....");
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("return exchange: {}, routingKey: {}, replyCode: {}, replyText: {}", exchange, routingKey, replyCode, replyText);
    }

}
