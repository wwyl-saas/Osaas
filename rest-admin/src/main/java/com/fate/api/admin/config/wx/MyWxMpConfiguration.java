package com.fate.api.admin.config.wx;

import com.fate.api.admin.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: parent
 * @description: 微信公众号功能配置
 * @author: chenyixin
 * @create: 2019-05-29 17:10
 **/
@Configuration
@Slf4j
@EnableConfigurationProperties(WxMpProperties.class)
public class MyWxMpConfiguration {
    @Autowired
    private WxMpProperties properties;
    @Autowired
    CacheService cacheService;
    @Autowired
    RedissonClient redissonClient;



    @Bean
    public WxMpService wxMpService() {
        MyMpRedisConfigImpl configStorage = new MyMpRedisConfigImpl();
        configStorage.setAppId(properties.getAppId());//设置微信公众号的appid
        configStorage.setSecret(properties.getSecret());//设置微信公众号的app secret
        configStorage.setToken(properties.getToken());//设置微信公众号的token
        configStorage.setAesKey(properties.getAesKey());//设置微信公众号的EncodingAESKey
        configStorage.setMpId(properties.getAppId());
        configStorage.setCacheService(cacheService);
        configStorage.setRedissonClient(redissonClient);
        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(configStorage);
        return service;
    }

    @Bean
    public WxMpMessageRouter wxMpMessageRouter(@Qualifier("wxMpService") WxMpService wxMpService) {
        final WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
        return wxMpMessageRouter;
    }

}
