package com.fate.api.customer.handler.wx.applet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fate.api.customer.service.CustomerService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.util.DateUtil;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.enums.CardWechatFieldType;
import me.chanjar.weixin.mp.bean.membercard.NameValues;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardActivatedMessage;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUserInfoResult;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @program: rest-customer
 * @description: 用户提交会员卡激活信息后，微信公众平台推送通知后的操作
 * @author: xudongdong
 * @create: 2019-07-21 23:20
 **/
@Component
@Slf4j
public class WaMemberCardHandler implements WxMpMessageHandler {
    @Autowired
    CustomerService customerService;
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        MerchantApplication application=CurrentApplicationUtil.getMerchantApplication();
        log.info("OpenID:"+wxMessage.getFromUser()+"发送激活会员卡信息，卡号："+wxMessage.getUserCardCode());
        Customer customer = customerService.getByOpenId(wxMessage.getFromUser(), application.getId());
        Assert.notNull(customer,"查询用户不存在");
        WxMpMemberCardActivatedMessage wxMpMemberCardActivatedMessage=new WxMpMemberCardActivatedMessage();
        wxMpMemberCardActivatedMessage.setCardId(wxMessage.getCardId());
        wxMpMemberCardActivatedMessage.setCode(wxMessage.getUserCardCode());
        wxMpMemberCardActivatedMessage.setMembershipNumber(customer.getId().toString());
        String result = wxMpService.getMemberCardService().activateMemberCard(wxMpMemberCardActivatedMessage);
        JSONObject jsonObject = JSON.parseObject(result);

        Integer errorCode = jsonObject.getInteger("errcode");
        if(errorCode!=null&&errorCode==0){//激活成功
            log.info("激活会员卡成功：OpenID:"+wxMessage.getFromUser()+"，卡号："+wxMessage.getUserCardCode());

            WxMpMemberCardUserInfoResult userInfo = wxMpService.getMemberCardService().getUserInfo(wxMessage.getCardId(), wxMessage.getUserCardCode());
            if("0".equals(userInfo.getErrorCode())){
                log.info("拉取会员信息成功：OpenID:"+wxMessage.getFromUser()+"，卡号："+wxMessage.getUserCardCode());
                NameValues[] commonFieldList = userInfo.getUserInfo().getCommonFieldList();
                for (NameValues nameValues : commonFieldList) {
                    if(CardWechatFieldType.USER_FORM_INFO_FLAG_MOBILE.equals(nameValues.getName())){
                        customer.setMobile(nameValues.getValue());
                    }
                    if(CardWechatFieldType.USER_FORM_INFO_FLAG_NAME.equals(nameValues.getName())){
                        customer.setName(nameValues.getValue());
                    }
                    if(CardWechatFieldType.USER_FORM_INFO_FLAG_BIRTHDAY.equals(nameValues.getName())){
                        log.info("生日的格式：{}",nameValues.getValue());
//                        customer.setBirthday(DateUtil.getLocalDate(nameValues.getValue()));//todo
                    }
                }
                customerService.updateCustomer(customer);
            }else{
                log.error("拉取会员信息失败：OpenID:{}，卡号：{},错误信息：{}",wxMessage.getFromUser(),wxMessage.getUserCardCode(),userInfo);
            }
        }else{
            log.error("激活会员卡失败：OpenID:{}，卡号：{},错误信息：{}",wxMessage.getFromUser(),wxMessage.getUserCardCode(),result);
        }
        return null;

    }
}
