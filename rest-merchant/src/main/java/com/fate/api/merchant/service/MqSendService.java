package com.fate.api.merchant.service;


import com.fate.api.merchant.config.mq.RabbitCallbackTemplate;
import com.fate.common.cons.MqConst;
import com.fate.common.model.MqProperties;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @program: parent
 * @description: 消息队列发送
 * @author: chenyixin
 * @create: 2019-09-23 12:57
 **/
@Slf4j
@Service
public class MqSendService{
    @Autowired
    Environment environment;
    @Autowired
    RabbitCallbackTemplate rabbitCallbackTemplate;


    /**
     * 发送到merchant的消息
     * @param message
     * @param mqProperties
     * @throws Exception
     */
    public void sendToCustomer(Object message, MqProperties mqProperties){
        try {
            Map<String, Object> properties= BeanUtil.objectToMap(mqProperties);
            MessageHeaders mhs = new MessageHeaders(properties);
            org.springframework.messaging.Message<Object> msg = MessageBuilder.createMessage(message, mhs);
            CorrelationData correlationData = new CorrelationData(String.valueOf(IdUtil.nextId()));
            if (mqProperties.getDelay()!=null){
                sendWithDelay(getExchanger(), "customer", msg, correlationData,mqProperties.getDelay());
            }else{
                rabbitCallbackTemplate.getRabbitTemplate().convertAndSend(getExchanger(), "customer", msg, correlationData);
            }
        }catch (Exception e){
            log.error("发送MQ消息失败",e);
        }

    }

    /**
     * 发送到admin的消息
     * @param message
     * @param mqProperties
     * @throws Exception
     */
    public void sendToAdmin(Object message,  MqProperties mqProperties) {
        try {
            Map<String, Object> properties= BeanUtil.objectToMap(mqProperties);
            MessageHeaders mhs = new MessageHeaders(properties);
            org.springframework.messaging.Message<Object> msg = MessageBuilder.createMessage(message, mhs);
            CorrelationData correlationData = new CorrelationData(String.valueOf(IdUtil.nextId()));
            if (mqProperties.getDelay()!=null){
                sendWithDelay(getExchanger(), "admin", msg, correlationData, (Integer) mqProperties.getDelay());
            }else{
                rabbitCallbackTemplate.getRabbitTemplate().convertAndSend(getExchanger(), "admin", msg, correlationData);
            }
        }catch (Exception e){
            log.error("发送MQ消息失败",e);
        }

    }

    /**
     * 延迟发送
     * @param exchange
     * @param routingKey
     * @param object
     * @param correlationData
     * @param seconds
     */
    private void sendWithDelay(String exchange, String routingKey, Object object,CorrelationData correlationData,Integer seconds){
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                rabbitCallbackTemplate.getRabbitTemplate().convertAndSend(exchange, routingKey, object, correlationData);
            }
        }, seconds*1000);// 毫秒
    }


    /**
     * 获取交换器
     * @return
     */
    public String getExchanger(){
        String exchange= MqConst.DEV_MERCHANT_EXCHANGER;
        if (environment.getActiveProfiles().length>0){
            String[] array=environment.getActiveProfiles();
            for(int i=0;i<array.length;i++){
                if (array[i].equals("online")){
                    return MqConst.ONLINE_MERCHANT_EXCHANGER;
                }else if (array[i].equals("test")){
                    return MqConst.TEST_MERCHANT_EXCHANGER;
                }else if (array[i].equals("dev")){
                    return MqConst.DEV_MERCHANT_EXCHANGER;
                }
            }
        }
        return exchange;
    }



}
