package com.fate.api.admin;

import com.fate.api.admin.service.WxMpMenuService;
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
 * @create: 2019-09-16 22:25
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class TestMpMenu {
    @Autowired
    WxMpMenuService wxMpMenuService;
    @Test
    public void testCreateCard() throws WxErrorException, IOException {
        wxMpMenuService.createMenu();
    }
}
