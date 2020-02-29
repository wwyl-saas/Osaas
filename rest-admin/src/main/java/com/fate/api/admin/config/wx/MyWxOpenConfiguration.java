package com.fate.api.admin.config.wx;

import com.fate.api.admin.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: parent
 * @description: 微信开放平台设置
 * @author: chenyixin
 * @create: 2019-09-05 12:46
 **/
@Slf4j
@Configuration
public class MyWxOpenConfiguration {
    @Autowired
    CacheService cacheService;

    @Value("${wx.open.componentAppId}")
    private String componentAppId;
    @Value("${wx.open.componentSecret}")
    private String componentSecret;
    @Value("${wx.open.componentToken}")
    private String componentToken;
    @Value("${wx.open.componentAesKey}")
    private String componentAesKey;

    @Bean
    public WxOpenService wxOpenService(){
        MyOpenRedisConfigImpl myOpenRedisConfig=new MyOpenRedisConfigImpl(cacheService);
        myOpenRedisConfig.setComponentAppId(componentAppId);
        myOpenRedisConfig.setComponentAppSecret(componentSecret);
        myOpenRedisConfig.setComponentToken(componentToken);
        myOpenRedisConfig.setComponentAesKey(componentAesKey);
        WxOpenServiceImpl wxOpenService=new WxOpenServiceImpl();
        wxOpenService.setWxOpenConfigStorage(myOpenRedisConfig);
        return wxOpenService;


    }

    @Bean
    public WxOpenMessageRouter wxOpenMessageRouter(@Qualifier("wxOpenService") WxOpenService wxOpenService) {
        WxOpenMessageRouter wxOpenMessageRouter = new WxOpenMessageRouter(wxOpenService);
        wxOpenMessageRouter.rule().handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            log.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        }).next();
        return wxOpenMessageRouter;
    }

}
