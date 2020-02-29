package com.fate.api.admin.service;

import com.fate.common.entity.CustomerApplication;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.*;
import me.chanjar.weixin.mp.bean.card.enums.CardWechatFieldType;
import me.chanjar.weixin.mp.bean.membercard.*;
import me.chanjar.weixin.mp.enums.WxMpApiUrl;
import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @program: parent
 * @description: 微信会员卡
 * @author: chenyixin
 * @create: 2019-09-05 12:55
 **/
@Service
@Slf4j
public class WxMemberCardService {
    @Autowired
    WxMpService wxMpService;

    public String createCard() throws WxErrorException, IOException {
        //调用其getResourceAsStream，得到一个InputStream
        InputStream input = WxMemberCardService.class.getClassLoader().getResourceAsStream("memberCard.json");
        //读取输入流，转成字符串
        String s = IOUtils.toString(input, Charset.forName("UTF-8"));

        MemberCard memberCard =MemberCard.fromJson(s);

        MemberCardCreateRequest cardCreateRequest=new MemberCardCreateRequest();
        cardCreateRequest.setMemberCard(memberCard);
        WxMpMemberCardCreateMessage createMessage=new WxMpMemberCardCreateMessage();
        createMessage.setCardCreateRequest(cardCreateRequest);
        final Gson GSON = WxMpGsonBuilder.create();
        String response = wxMpService.post(WxMpApiUrl.MemberCard.MEMBER_CARD_CREATE,  GSON.toJson(createMessage));
        WxMpCardCreateResult wxMpCardCreateResult= WxMpCardCreateResult.fromJson(response);
        log.info(wxMpCardCreateResult.getCardId());
        return wxMpCardCreateResult.getCardId();
    }


    /**
     * 设置白名单
     * @param customerId
     * @throws WxErrorException
     */
    public void setWhiteList(Long customerId) throws WxErrorException {
        CustomerApplication customerApplication=null;
        String result=wxMpService.getCardService().addTestWhiteList(customerApplication.getWxOpenid());
    }

    /**
     * 设置开卡字段
     */
    public void setOpenInfo(String cardId) throws WxErrorException {
        MemberCardActivateUserFormRequest userFormRequest=new MemberCardActivateUserFormRequest();
        userFormRequest.setCardId(cardId);
        //todo 会员守则
        userFormRequest.setBindOldCard("会员守则","https://baidu.com");
        //todo 老会员数据绑定入口
//        userFormRequest.setBindOldCard("老会员绑定","https://m.wanwuyoulian.com/old");
        MemberCardUserForm memberCardUserForm=new MemberCardUserForm();
        memberCardUserForm.setCanModify(false);
        memberCardUserForm.addWechatField(CardWechatFieldType.USER_FORM_INFO_FLAG_MOBILE);
        memberCardUserForm.addWechatField(CardWechatFieldType.USER_FORM_INFO_FLAG_NAME);
        memberCardUserForm.addWechatField(CardWechatFieldType.USER_FORM_INFO_FLAG_BIRTHDAY);
        userFormRequest.setRequiredForm(memberCardUserForm);
        MemberCardActivateUserFormResult result=wxMpService.getMemberCardService().setActivateUserForm(userFormRequest);
        log.info(result.toString());
    }

    /**
     * 获取跳转到微信开卡插件参数
     */
    public ActivatePluginParam getPluginParam() throws WxErrorException {
        return wxMpService.getMemberCardService().getActivatePluginParam("pX6tV1oADq8QWIjFCyK9zaTRoJTc", "test");
    }

    /**
     * 获取激活后的用户信息
     * @param activateTicket
     * @return
     * @throws WxErrorException
     */
    public WxMpMemberCardActivateTempInfoResult getActivateTempInfo(String activateTicket) throws WxErrorException {
        return wxMpService.getMemberCardService().getActivateTempInfo(activateTicket);
    }

    /**
     * 激活会员卡
     */
    public void activateMemberCard() throws WxErrorException {
        WxMpMemberCardActivatedMessage activatedMessage = new WxMpMemberCardActivatedMessage();
        activatedMessage.setMembershipNumber("pX6tV1oADq8QWIjFCyK9zaTRoJTc");
        activatedMessage.setCode("");
        activatedMessage.setCardId("");
        activatedMessage.setInitBonus(2000);
        activatedMessage.setInitBonusRecord("测试激活送积分");
        String response = wxMpService.getMemberCardService().activateMemberCard(activatedMessage);
    }

    /**
     * 删除会员卡
     * @param cardId
     */
    public void delete(String cardId) throws WxErrorException {
        WxMpCardDeleteResult result=wxMpService.getCardService().deleteCard(cardId);
        log.info("删除结果：",result);
    }

    /**
     * 更新会员卡
     * @param cardId
     */
    public void update(String cardId) throws WxErrorException {
        WxMpMemberCardUpdateMessage updateMessage = new WxMpMemberCardUpdateMessage();
//        updateMessage.setBackgroundPicUrl("http://mmbiz.qpic.cn/mmbiz_png/Bl39GIbRIyJa6XGFNic5fG0ia96fTzffXdBKZLBmFpEdaIc0XTcajnIvRXOm0stiaFjyrFFTxCAWicy1UZ8SfGutPg/0");
        updateMessage.setAddBounus(100);
        updateMessage.setBonus(1000);
//        updateMessage.setAddBalance(200D);
//        updateMessage.setBalance(200D);
//        updateMessage.setRecordBalance("测试充值100，重返100");
        updateMessage.setCustomFieldValue1("普通");
        updateMessage.setCardId(cardId);
        updateMessage.setCode("297101743481");
        WxMpMemberCardUpdateResult result = wxMpService.getMemberCardService().updateUserMemberCard(updateMessage);
        log.info("返回结果{}",result);
    }
}
