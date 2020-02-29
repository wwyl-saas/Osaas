package com.fate.api.customer.handler.wx.mp;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: parent
 * @description: 微信公众号关注事件
 * @author: chenyixin
 * @create: 2019-09-03 11:01
 **/
@Component
@Slf4j
public class WxSubscribeHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        //todo 为了满足测试企业商户端用户订阅的问题，请兼容applicationId=3的情况，去更新商户用户表
        return null;
    }
}
