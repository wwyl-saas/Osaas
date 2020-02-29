package com.fate.api.customer.handler.wx.applet;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSON;
import com.fate.api.customer.config.wx.WxMaConfiguration;
import com.fate.api.customer.handler.LoginHandler;
import com.fate.api.customer.query.LoginQuery;
import com.fate.api.customer.query.TryLoginQuery;
import com.fate.api.customer.service.CustomerService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerApplication;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;



/**
 * @program: parent
 * @description: 微信小程序登录handler
 * @author: chenyixin
 * @create: 2019-06-21 11:11
 **/
@Component
@Slf4j
public class WaLoginHandler implements LoginHandler {
    @Autowired
    CustomerService customerService;

    /**
     * 尝试登录
     *
     * @param query
     * @return
     */
    @Override
    public Customer tryLogin(TryLoginQuery query) {
        final WxMaService wxService = WxMaConfiguration.getMaService(CurrentApplicationUtil.getMerchantApplication().getAppId());
        WxMaJscode2SessionResult session;
        try {
            session = wxService.getUserService().getSessionInfo(query.getLoginCode());
        } catch (WxErrorException e) {
            log.error("微信code2session接口出错", e);
            throw new BaseException(ResponseInfo.WX_CODE_SESSION_ERROR);
        }
        Assert.notNull(session, "请求微信获取session为null");
        log.info("微信返回session:" + JSON.toJSONString(session));
        Customer customer = getUniqueCustomer(session);
        return customer;
    }

    /**
     * 授权登录（不管用户是否已经存在，强制性更新用户信息）
     *
     * @param query
     * @return
     */
    @Override
    public Customer login(LoginQuery query) {
        MerchantApplication application=CurrentApplicationUtil.getMerchantApplication();
        final WxMaService wxService = WxMaConfiguration.getMaService(application.getAppId());
        WxMaJscode2SessionResult session = getSession(wxService, query.getLoginCode());
        Assert.notNull(session, "请求微信获取session为null");
        log.info("微信返回session:" + JSON.toJSONString(session));
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(session.getSessionKey(), query.getRawData(), query.getSignature())) {
            throw new BaseException(ResponseInfo.WX_VALIDATE_ERROR);
        }
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(session.getSessionKey(), query.getEncryptedData(), query.getIv());
        Assert.notNull(userInfo, "微信返回用户信息为空");

        Customer customer = getUniqueCustomer(session);
        if (customer == null) {
            customer = customerService.createCustomer(userInfo,session.getUnionid());
        } else {
            customer = customerService.changeCustomerInfo(customer, userInfo,session.getUnionid());
        }
        relateApplication(application,customer,session.getOpenid());
        return customer;
    }


    /**
     * 获取session信息
     *
     * @param wxService
     * @param code
     * @return
     */
    private WxMaJscode2SessionResult getSession(WxMaService wxService, String code) {
        WxMaJscode2SessionResult session;
        try {
            session = wxService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            log.error("微信code2session接口出错", e);
            throw new BaseException(ResponseInfo.WX_CODE_SESSION_ERROR);
        }
        return session;
    }

    /**
     * 获取唯一的用户
     *
     * @param session
     * @return
     */
    private Customer getUniqueCustomer(WxMaJscode2SessionResult session) {
        MerchantApplication application=CurrentApplicationUtil.getMerchantApplication();
        Customer customer = null;
        if (StringUtils.isNotBlank(session.getUnionid())) { //有关联应用
            customer = customerService.getByUnionId(session.getUnionid());
            if (customer == null) {
                customer=customerService.getByOpenId(session.getOpenid(),application.getId());
            }
        }else {
            customer=customerService.getByOpenId(session.getOpenid(),application.getId());
        }
        if (customer != null) {//用户已存在，则关联应用信息
            customer.setWxUnoinid(session.getUnionid());
        }
        return customer;
    }


    /**
     * 关联此应用的信息
     *
     * @param application
     * @param customer
     * @param wxOpenId
     * @return
     */
    private CustomerApplication relateApplication(MerchantApplication application, Customer customer, String wxOpenId) {
        CustomerApplication customerApplication = customerService.getCustomerApplicationByApplicationIdAndOpenId(wxOpenId, application.getId());
        if (customerApplication == null) {
            customerApplication = new CustomerApplication();
            customerApplication.setMerchantAppId(application.getId())
                    .setCustomerId(customer.getId())
                    .setMerchantAppType(application.getType())
                    .setWxOpenid(wxOpenId);
            Assert.isTrue(customerApplication.insert(), "插入数据失败");
        }
        return customerApplication;
    }

}
