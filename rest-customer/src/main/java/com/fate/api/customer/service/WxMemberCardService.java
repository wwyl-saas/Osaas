package com.fate.api.customer.service;

import com.alibaba.fastjson.JSONObject;
import com.fate.api.customer.config.wx.WxMpConfiguration;
import com.fate.api.customer.dto.CustomerDto;
import com.fate.api.customer.dto.PluginParamDto;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerAccount;
import com.fate.common.entity.Member;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ApplicationType;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.DateUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.enums.CardWechatFieldType;
import me.chanjar.weixin.mp.bean.membercard.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 微信会员卡
 * @author: chenyixin
 * @create: 2019-09-07 20:09
 **/
@Service
@Slf4j
public class WxMemberCardService {
    @Autowired
    MerchantService merchantService;
    @Autowired
    CustomerService customerService;
    @Autowired
    MemberService memberService;
    @Autowired
    CustomerAccountService customerAccountService;

    /**
     * 获取跳转到微信开卡插件参数
     * @param cardId
     */
    public PluginParamDto getPluginParam(String cardId) throws WxErrorException {
        CustomerAccount customerAccount=customerAccountService.findByCustomerId(CurrentCustomerUtil.getCustomer().getId());
        Assert.isNull(customerAccount,"会员账户已存在，不能重复领取");
        MerchantApplication merchantApplication= CurrentApplicationUtil.getMerchantApplication();
        String appId=merchantApplication.getAppId();
        if (!merchantApplication.getType().equals(ApplicationType.WX_PUBLIC)){
            //此处要求只维护商家一个公众号
            MerchantApplication publicApplication=merchantService.getMpApplication();
            Assert.notNull(publicApplication,"商户的服务号未配置");
            appId=publicApplication.getAppId();
        }
        WxMpService wxMpService= WxMpConfiguration.getMpService(appId);
        ActivatePluginParam activatePluginParam= wxMpService.getMemberCardService().getActivatePluginParam(cardId, String.valueOf(ApplicationType.WX_APPLET.getCode()));
        return BeanUtil.mapper(activatePluginParam,PluginParamDto.class);
    }


    /**
     * 激活会员卡返回用户信息
     * @param activateTicket
     * @return
     * @throws WxErrorException
     */
    public CustomerDto getActivateMemberCard(String activateTicket, String cardId, String cardCode) throws WxErrorException {
        Customer customer= CurrentCustomerUtil.getCustomer();
        WxMpService wxMpService= WxMpConfiguration.getMpService(getPublicAppid());

        //获取用户提交的信息
        WxMpMemberCardActivateTempInfoResult tempInfoResult=wxMpService.getMemberCardService().getActivateTempInfo(activateTicket);

        Assert.notNull(tempInfoResult,"微信返回用户结果为空");
        Assert.isTrue(tempInfoResult.getErrorCode().equals("0"),"微信返回结果报错");
        log.info("微信返回用户信息：{}",tempInfoResult);
        MemberCardUserInfo userInfo=tempInfoResult.getUserInfo();
        NameValues[] commonFieldList = userInfo.getCommonFieldList();
        for (NameValues nameValues : commonFieldList) {
            if(CardWechatFieldType.USER_FORM_INFO_FLAG_MOBILE.name().equals(nameValues.getName())){
                customer.setMobile(nameValues.getValue());
            }
            if(CardWechatFieldType.USER_FORM_INFO_FLAG_NAME.name().equals(nameValues.getName())){
                customer.setName(nameValues.getValue());
            }
            if(CardWechatFieldType.USER_FORM_INFO_FLAG_BIRTHDAY.name().equals(nameValues.getName())){
                if (nameValues.getValue().trim().length()<10){
                    String[] array=nameValues.getValue().split("-");
                    List<String> list=Arrays.stream(array).map(num-> String.format("%02d", Integer.parseInt(num))).collect(Collectors.toList());
                    String newDate= Joiner.on("-").join(list);
                    customer.setBirthday(DateUtil.getLocalDate(newDate));
                }else{
                    customer.setBirthday(DateUtil.getLocalDate(nameValues.getValue()));
                }

            }
        }
        customer.setUpdateTime(null);
        Assert.isTrue(customerService.updateCustomer(customer),"插入更新失败");

        //激活会员卡
        Integer initBonus=10;
        WxMpMemberCardActivatedMessage activatedMessage = new WxMpMemberCardActivatedMessage();
        activatedMessage.setMembershipNumber(cardCode);
        activatedMessage.setCode(cardCode);
        activatedMessage.setCardId(cardId);
        activatedMessage.setInitBonus(initBonus);
        activatedMessage.setInitBonusRecord("新会员赠送"+initBonus+"积分");
        String response = wxMpService.getMemberCardService().activateMemberCard(activatedMessage);
        log.info("微信返回会员卡激活结果：{}",response);
        JSONObject result= JSONObject.parseObject(response);
        Assert.isTrue(result.getInteger("errcode")==0,"激活会员卡返回失败");

        //创建会员账户
        CustomerAccount account=customerAccountService.createAccount(customer,initBonus,cardCode);
        Assert.notNull(account,"账户不存在");
        //修改会员卡信息
        Member member=memberService.getById(account.getMemberId());
        Assert.notNull(member,"商户会员卡信息未配置");
        WxMpMemberCardUpdateMessage updateMessage=new WxMpMemberCardUpdateMessage();
        if (StringUtils.isNotBlank(member.getWxPictureUrl())){
            updateMessage.setBackgroundPicUrl(member.getWxPictureUrl());
        }
        updateMessage.setCustomFieldValue1(member.getLevel().getDesc());
        updateMessage.setCardId(cardId);
        updateMessage.setCode(cardCode);
        WxMpMemberCardUpdateResult updateResult = wxMpService.getMemberCardService().updateUserMemberCard(updateMessage);
        Assert.isTrue(updateResult.getErrorCode().equals("0"),"更新会员卡返回失败");

        //返回结果
        CustomerDto customerDto = BeanUtil.mapper(customer,CustomerDto.class);
        customerDto.setBalance(account.getBalance());
        customerDto.setConsumeScore(account.getConsumeScore());
        customerDto.setMemberId(customer.getId().toString());
        customerDto.setMemberLevel(account.getMemberLevel().getCode());
        customerDto.setMemberLevelName(account.getMemberLevel().getDesc());
        return customerDto;
    }

    /**
     * 获取公众号ID
     * @return
     */
    private String getPublicAppid(){
        MerchantApplication merchantApplication= CurrentApplicationUtil.getMerchantApplication();
        String appId=merchantApplication.getAppId();
        if (!merchantApplication.getType().equals(ApplicationType.WX_PUBLIC)){
            //此处要求只维护商家一个公众号
            MerchantApplication publicApplication=merchantService.getMpApplication();
            Assert.notNull(publicApplication,"商户的公众号未配置");
            appId=publicApplication.getAppId();
        }
        return appId;
    }


    
}
