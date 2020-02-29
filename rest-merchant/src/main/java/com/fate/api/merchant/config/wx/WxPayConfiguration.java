package com.fate.api.merchant.config.wx;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: parent
 * @description: 微信支付配置
 * @author: chenyixin
 * @create: 2019-05-29 17:12
 **/
//@Configuration
@Slf4j
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {
    @Autowired
    private WxPayProperties properties;

    @Bean
    public WxPayService wxPayService(){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(properties.getAppId());
        payConfig.setMchId(properties.getMchId()); //微信支付商户号
        payConfig.setMchKey(properties.getMchKey()); //微信支付商户密钥
        payConfig.setSubAppId(null);//服务商模式下的子商户公众账号ID，普通模式请不要配置
        payConfig.setSubMchId(null); //服务商模式下的子商户号，普通模式请不要配置
        payConfig.setKeyPath(properties.getKeyPath()); //apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定

        // 使用沙箱环境
        payConfig.setUseSandboxEnv(true);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
