package com.fate.api.merchant.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSON;
import com.fate.api.merchant.dto.MerchantUserDto;
import com.fate.api.merchant.query.AdminLoginQuery;
import com.fate.api.merchant.query.TryLoginQuery;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.entity.Subscriber;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.enums.SmsEnum;
import com.fate.api.merchant.event.ShortMsgEvent;
import com.fate.api.merchant.query.LoginQuery;
import com.fate.common.entity.MerchantUser;
import com.fate.common.exception.BaseException;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentRequestUtil;
import com.fate.common.util.RandomUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;


/**
 * @program: parent
 * @description: 登录相关
 * @create: 2019-05-22 11:43
 **/
@Service
@Slf4j
public class LoginService {

    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    CacheService cacheService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    WxMaService wxMaService;
    @Autowired
    SubscriberService subscriberService;

    private static final String SMS_SEND_KEY = "merchant:login:sms:";
    private static final String SMS_SEND_TIME = "merchant:login:time:";
    private static final int MINUTES=5;
    private static final int SECONDS=MINUTES*60;

    /**
     * 尝试微信登录（减少重复的登录次数）
     * @param query
     * @return
     */
    public MerchantUserDto tryLogin(TryLoginQuery query) {
        WxMaJscode2SessionResult session;
        try {
            session = wxMaService.getUserService().getSessionInfo(query.getLoginCode());
        } catch (WxErrorException e) {
            log.error("微信code2session接口出错", e);
            throw new BaseException(ResponseInfo.WX_CODE_SESSION_ERROR);
        }
        Assert.notNull(session, "请求微信获取session为null");
        log.info("微信返回session:" + JSON.toJSONString(session));
        //通过绑定的企业小程序的openId登录
        MerchantUser user = merchantUserService.getByAppletOpenId(session.getOpenid());
        Assert.notNull(user,"无绑定信息，请登录");
        if (StringUtils.isNotBlank(session.getUnionid())){
            user.setWxUnionid(session.getUnionid());
            if (StringUtils.isBlank(user.getWxMpOpenid())){
                bindMpSubscriber(user);
            }
        }

        return logAndTranslate(user);
    }

    /**
     * 绑定微信公众号的openId
     * @param user
     */
    private void bindMpSubscriber(MerchantUser user) {
        Subscriber subscriber=subscriberService.getByUnionId(user.getWxUnionid());
        if (subscriber!=null){//已经订阅
            user.setWxMpOpenid(subscriber.getWxOpenid());
        }
    }


    /**
     * 发送短信
     * @param phone
     * @return
     */
    public void sendSmsCaptcha(String phone) {
        //先检查人员是否存在
        MerchantUser merchantUser=merchantUserService.getByPhone(phone);
        Assert.notNull(merchantUser,ResponseInfo.USER_NONE.getMsg());

        //防止多次调用
        String ip= CurrentRequestUtil.getCurrentClientIp();
        Long times = (Long) cacheService.get(SMS_SEND_TIME + ip);
        if (times!=null) {
            if (times>3){
                throw new BaseException(ResponseInfo.SMSCAPTCHA_OVER_LIMIT);
            }
        }else {
            times=0L;
        }

        String smsCaptcha = RandomUtil.getRandomStringLongRange(100000,999999);
        cacheService.set(SMS_SEND_KEY + phone, smsCaptcha,SECONDS);
        cacheService.set(SMS_SEND_TIME+ip,times+1,SECONDS);
        Map<String,Object> params = Maps.newHashMap();
        params.put("code",smsCaptcha);
        params.put("minutes",MINUTES);
        ShortMsgEvent msgEvent = new ShortMsgEvent(this);
        msgEvent.setPhones(new String[]{phone});
        msgEvent.setParams(params);
        msgEvent.setTemplate(SmsEnum.CAPTCHA.getContext());
        applicationContext.publishEvent(msgEvent);
    }


    /**
     * 手机号密码登录
     * @param query
     * @return
             */
    public MerchantUserDto login(AdminLoginQuery query) {
        Assert.notNull(query.getPwd(),"密码不能为空");

        MerchantUser user = merchantUserService.getByPhone(query.getPhone());
        Assert.notNull(user,ResponseInfo.USER_NONE.getMsg());
        Assert.isTrue(query.getPwd().equals(user.getPassword()),ResponseInfo.USERNAME_OR_PWD_ERROR.getMsg());
        return logAndTranslate(user);
    }

    /**
     * 手机号短信 授权登录
     * @param query
     * @return
     */
    public MerchantUserDto mobileLogin(LoginQuery query) {
        Assert.notNull(query.getSmsCode(),"验证码为空");
        WxMaJscode2SessionResult session=getSession(wxMaService,query.getLoginCode());
        Assert.notNull(session,"请求微信获取session为null");
        log.debug("微信返回session:"+ JSON.toJSONString(session));

        MerchantUser user = merchantUserService.getByPhone(query.getPhone());
        Assert.notNull(user,ResponseInfo.USER_NONE.getMsg());
        //验证短信验证码
        String sms = (String) cacheService.get(SMS_SEND_KEY + query.getPhone());
        Assert.notNull(sms,ResponseInfo.MESSAGE_CODE_EXPIRE.getMsg());
        Assert.isTrue(sms.equals(query.getSmsCode().trim()),ResponseInfo.SMS_CODE_ERROR.getMsg());
        cacheService.del(SMS_SEND_KEY + query.getPhone());
        String ip= CurrentRequestUtil.getCurrentClientIp();
        cacheService.del(SMS_SEND_TIME + ip);
        //绑定微信
        if (StringUtils.isNotBlank(session.getUnionid())){
            user.setWxUnionid(session.getUnionid());
            if (StringUtils.isBlank(user.getWxMpOpenid())){
                bindMpSubscriber(user);
            }
        }
        user.setWxApOpenid(session.getOpenid());
        return logAndTranslate(user);
    }


    /**
     *  记录最后登录时间并返回用户信息
     * @param user
     * @return
     */
    private MerchantUserDto logAndTranslate(MerchantUser user) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setUpdateTime(null);
        merchantUserService.updateUser(user);
        return BeanUtil.mapper(user, MerchantUserDto.class);
    }



    /**
     * 获取session信息
     * @param wxService
     * @param code
     * @return
     */
    private WxMaJscode2SessionResult getSession(WxMaService wxService,String code){
        WxMaJscode2SessionResult session;
        try {
            session = wxService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            log.error("微信code2session接口出错",e);
            throw new BaseException(ResponseInfo.WX_CODE_SESSION_ERROR);
        }
        return session;
    }


    /**
     * 退出登录
     */
    public void logout() {
        MerchantUser user= CurrentUserUtil.getMerchantUser();
        user.setWxApOpenid("");
        user.setWxUnionid("");
        user.updateById();
    }
}