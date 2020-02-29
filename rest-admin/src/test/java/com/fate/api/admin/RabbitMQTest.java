package com.fate.api.admin;

import com.fate.common.enums.MessageTag;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-23 21:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class RabbitMQTest {
//    @Autowired
//    private MqSendService mqSendService;
//
//    @Test
//    public void testSendToMerchant() throws Exception {
//        MyMessage myMessage=new MyMessage(1L);
//        MqProperties mqProperties=MqProperties.builder().merchantId(1l).messageTag(MessageTag.CUSTOMER_ORDER_CANCEL.getTag()).build();
//        mqSendService.sendToMerchant(myMessage,mqProperties);
//    }
//
//    @Test
//    public void testSendToCustomer() throws Exception {
//        MyMessage myMessage=new MyMessage(1L);
//        MqProperties mqProperties=MqProperties.builder().merchantId(1l).messageTag(MessageTag.MERCHANT_APPOINTMENT_CANCEL.getTag()).build();
//        mqSendService.sendToCustomer1(myMessage,mqProperties);
//    }
//
//    @Test
//    public void testSendToAdmin1() throws Exception {
//        MyMessage myMessage=new MyMessage(1L);
//        MqProperties mqProperties=MqProperties.builder().merchantId(1l).messageTag(MessageTag.MERCHANT_APPOINTMENT_CANCEL.getTag()).build();
//        mqSendService.sendToAdmin(myMessage,mqProperties);
//    }
//
//
//    @Test
//    public void testSendToAdmin2() throws Exception {
//        MyMessage myMessage=new MyMessage(2L);
//        MqProperties mqProperties=MqProperties.builder().merchantId(1l).messageTag(MessageTag.MERCHANT_APPOINTMENT_CANCEL.getTag()).build();
//        mqSendService.sendToAdmin1(myMessage,mqProperties);
//    }
}
