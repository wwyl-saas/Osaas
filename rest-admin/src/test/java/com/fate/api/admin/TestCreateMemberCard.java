package com.fate.api.admin;

import com.fate.api.admin.service.WxMemberCardService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-15 00:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class TestCreateMemberCard {
    @Autowired
    WxMemberCardService wxMemberCardService;

    @Test
    public void testCreateCard() throws WxErrorException, IOException {
        String cardId=wxMemberCardService.createCard();
        wxMemberCardService.setOpenInfo(cardId);
    }

    @Test
    public void testUpdateCard() throws WxErrorException {
        wxMemberCardService.update("pX6tV1juztq-q7zLqBnnE3SajQdY");
    }
}
