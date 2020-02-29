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
 * @description: 微信公众号订阅事件
 * @author: chenyixin
 * @create: 2019-09-03 11:03
 **/
@Component
@Slf4j
public class WxSubscribeHandler implements WxMpMessageHandler {
    @Autowired
    SubscriberService subscriberService;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    MerchantService merchantService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String mpOpenId=wxMpXmlMessage.getFromUser();
        log.info("openId={}关注企业公众号",mpOpenId);
        WxMpUser wxMpUser=wxMpService.getUserService().userInfo(mpOpenId);
        log.info("关注者信息{}",wxMpUser);

        Subscriber subscriber=subscriberService.getByOpenid(mpOpenId);
        if (subscriber==null){//首次订阅
            subscriber=subscriberService.createSubcriber(wxMpUser);
            if (StringUtils.isNotBlank(subscriber.getWxUnoinid())){//关联商户用户（只有做了小程序绑定的才可以关联）
                bindMerchantUser(subscriber);
            }
        }else{//二次订阅
            subscriber.setSubscribe(wxMpUser.getSubscribe());
            subscriber.setAvatar(wxMpUser.getHeadImgUrl());
            subscriber.setNickname(wxMpUser.getNickname());
            subscriber.setWxUnoinid(wxMpUser.getUnionId());
            subscriber.setSource(wxMpUser.getSubscribeScene());
            subscriber.setUpdateTime(null);
            Assert.isTrue(subscriber.updateById(),"更新数据失败");
            if (StringUtils.isNotBlank(subscriber.getWxUnoinid())){//关联商户用户（只有做了小程序绑定的才可以关联）
                bindMerchantUser(subscriber);
            }
        }
        return null;
    }


    /**
     * 绑定商户用户
     * @param subscriber
     */
    private void bindMerchantUser(Subscriber subscriber){
        MerchantUser merchantUser=merchantUserService.getByUnionId(subscriber.getWxUnoinid());
        if (merchantUser!=null){
            Merchant merchant=merchantService.getMerchantById(merchantUser.getMerchantId());
            CurrentMerchantUtil.addMerchant(merchant);
            merchantUser.setWxMpOpenid(subscriber.getWxOpenid());
            merchantUser.setUpdateTime(null);
            Assert.isTrue(merchantUser.updateById(),"更新数据失败");
            CurrentMerchantUtil.remove();
            log.info("关联用户{}",merchantUser);
        }
    }

}
