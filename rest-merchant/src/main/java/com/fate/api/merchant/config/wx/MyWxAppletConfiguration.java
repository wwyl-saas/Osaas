package com.fate.api.merchant.config.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.fate.api.merchant.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 商户端小程序配置类
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(WxMaProperties.class)
public class MyWxAppletConfiguration {
    @Autowired
    private WxMaProperties properties;
    @Autowired
    CacheService cacheService;
    @Autowired
    RedissonClient redissonClient;

    @Bean
    public WxMaService wxMaService(){
        MyMaRedisConfigImpl config = new MyMaRedisConfigImpl();
        config.setAppid(properties.getAppId());//设置微信小程序的appid
        config.setSecret(properties.getSecret());//设置微信小程序的Secret
        config.setToken(properties.getToken());//设置微信小程序消息服务器配置的token
        config.setAesKey(properties.getAesKey());//设置微信小程序消息服务器配置的EncodingAESKey
        config.setMsgDataFormat(properties.getMsgDataFormat());//消息格式，XML或者JSON
        config.setMaId(properties.getAppId());
        config.setCacheService(cacheService);
        config.setRedissonClient(redissonClient);

        WxMaService wxMaService= new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }

    @Bean
    public WxMaMessageRouter wxMaMessageRouter(@Qualifier("wxMaService") WxMaService wxMaService){
        WxMaMessageRouter wxMaMessageRouter=new WxMaMessageRouter(wxMaService);
//        wxMaMessageRouter.rule().handler(logHandler).next()
//                .rule().async(false).content("模板").handler(templateMsgHandler).end()
//                .rule().async(false).content("文本").handler(textHandler).end()
//                .rule().async(false).content("图片").handler(picHandler).end()
//                .rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return wxMaMessageRouter;
    }


}
