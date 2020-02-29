package com.fate.api.customer.config.wx;

import com.fate.common.dao.MerchantApplicationDao;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ApplicationType;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: parent
 * @description: 微信支付配置
 * @author: chenyixin
 * @create: 2019-05-29 17:12
 **/
@Configuration
@DependsOn({"mybatisSqlSessionFactoryBean"})
@Slf4j
public class WxPayConfiguration {
    private static final String CERT_NAME="apiclient_cert.p12";
    @Value("${wx.pay.sandbox}")
    private boolean sandbox;
    @Resource
    private MerchantApplicationDao merchantApplicationDao;

    private static Map<String, WxPayService> payServices = Maps.newHashMap();


    public static WxPayService getPayService(String appid) {
        WxPayService wxService = payServices.get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return wxService;
    }

    @PostConstruct
    public void init() {
        List<MerchantApplication> applications = merchantApplicationDao.findAllEnable(Arrays.asList(ApplicationType.WX_APPLET, ApplicationType.WX_PUBLIC));
        Assert.notEmpty(applications, "配置数据不能为空");
        applications.stream().filter(merchantApplication -> StringUtils.isNotBlank(merchantApplication.getWxMchId()))
                .forEach(a -> {
                    WxPayConfig payConfig = new WxPayConfig();
                    payConfig.setAppId(a.getAppId());
                    payConfig.setMchId(a.getWxMchId()); //微信支付商户号
                    payConfig.setMchKey(a.getWxPartnerKey()); //微信支付商户密钥
                    payConfig.setSubAppId(null);//服务商模式下的子商户公众账号ID，普通模式请不要配置
                    payConfig.setSubMchId(null); //服务商模式下的子商户号，普通模式请不要配置
                    payConfig.setKeyPath("classpath:cert"+ File.separator+a.getMerchantId()+File.separator+CERT_NAME); //apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定

                    // 使用沙箱环境
                    payConfig.setUseSandboxEnv(sandbox);
                    WxPayService wxPayService = new WxPayServiceImpl();
                    wxPayService.setConfig(payConfig);
                    if (sandbox){
                        try {
                            final String signKey = wxPayService.getSandboxSignKey();
                            log.info("获取沙箱密码为：{}",signKey);
                            payConfig.setMchKey(signKey);
                        } catch (WxPayException e) {
                           log.error("{}获取沙箱环境密钥失败",a,e);
                        }
                    }
                    payServices.put(a.getAppId(), wxPayService);
                });
    }

}
