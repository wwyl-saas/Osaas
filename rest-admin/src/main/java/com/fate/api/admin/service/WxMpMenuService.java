package com.fate.api.admin.service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: parent
 * @description: 公众号菜单
 * @author: chenyixin
 * @create: 2019-09-16 22:07
 **/
@Service
@Slf4j
public class WxMpMenuService {
    @Autowired
    WxMpService wxMpService;

    public void createMenu() throws IOException, WxErrorException {
        WxMenu wxMenu=new WxMenu();
        List<WxMenuButton> buttons = new ArrayList();
        WxMenuButton wxMenuButton=new WxMenuButton();
        wxMenuButton.setType("miniprogram");
        wxMenuButton.setName("商户端");
        wxMenuButton.setUrl("http://mp.weixin.qq.com");
        wxMenuButton.setAppId("wx546948ba95fe71ec");
        wxMenuButton.setPagePath("pages/login/login");
        buttons.add(wxMenuButton);
        wxMenu.setButtons(buttons);
        wxMpService.getMenuService().menuDelete();
        String result=wxMpService.getMenuService().menuCreate(wxMenu);
        log.info(result);
    }


}
