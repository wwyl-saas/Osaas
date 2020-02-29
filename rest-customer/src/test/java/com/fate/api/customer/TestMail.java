package com.fate.api.customer;

import com.fate.api.customer.event.MailMsgEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-05-28 17:17
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class TestMail {
    @Autowired
    ApplicationContext applicationContext;

//    @Test
    public void testSimple(){
        MailMsgEvent mailMsgEvent =new MailMsgEvent(this);
        mailMsgEvent.setTo(new String[]{"chenxin@wanwuyoulian.com"});
        mailMsgEvent.setSubject("主题：简单邮件");
        mailMsgEvent.setContent("测试邮件内容");
        applicationContext.publishEvent(mailMsgEvent);
    }

    @Test
    public void test(){

    }
}
