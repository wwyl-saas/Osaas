package com.fate.api.merchant.handler;

import com.fate.api.merchant.service.MerchantService;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.api.merchant.service.SubscriberService;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.Subscriber;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @program: parent
 * @description: 微信公众号取消订阅
 * @author: chenyixin
 * @create: 2019-09-16 19:53
 **/
@Component
@Slf4j
public class WxUnSubscribeHandler  implements WxMpMessageHandler {
    @Autowired
    SubscriberService subscriberService;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    MerchantService merchantService;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String mpOpenId=wxMessage.getFromUser();
        log.info("openId={}关注企业公众号",mpOpenId);
        WxMpUser wxMpUser=wxMpService.getUserService().userInfo(mpOpenId);
        log.info("关注者信息{}",wxMpUser);
        Subscriber subscriber=subscriberService.getByOpenid(mpOpenId);
        Assert.notNull(subscriber,"订阅者不存在");
        subscriber.setUpdateTime(null);
        subscriber.setSubscribe(false);
        Assert.isTrue(subscriber.updateById(),"更新数据失败");

        if (StringUtils.isNotBlank(subscriber.getWxUnoinid())){//解绑商户用户
            MerchantUser merchantUser=merchantUserService.getByUnionId(subscriber.getWxUnoinid());
            if (merchantUser!=null){
                Merchant merchant=merchantService.getMerchantById(merchantUser.getMerchantId());
                CurrentMerchantUtil.addMerchant(merchant);
                merchantUser.setWxMpOpenid(null);
                merchantUser.setUpdateTime(null);
                Assert.isTrue(merchantUser.updateById(),"更新数据失败");
                CurrentMerchantUtil.remove();
                log.info("解绑用户{}",merchantUser);
            }
        }

        return null;
    }
}
