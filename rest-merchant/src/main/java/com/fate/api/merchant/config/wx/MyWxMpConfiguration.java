package com.fate.api.merchant.config.wx;

import com.fate.api.merchant.handler.WxSubscribeHandler;
import com.fate.api.merchant.handler.WxUnSubscribeHandler;
import com.fate.api.merchant.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: parent
 * @description: 企业自身微信公众号功能配置
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
    @Autowired
    WxSubscribeHandler wxSubscribeHandler;
    @Autowired
    WxUnSubscribeHandler wxUnSubscribeHandler;


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
        // 关注事件
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(wxSubscribeHandler).end();
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.UNSUBSCRIBE).handler(wxUnSubscribeHandler).end();

        return wxMpMessageRouter;
    }


//    public WxMpMessageRouter newRouter(WxMpService service) {

//
//        // 记录所有事件的日志 （异步执行）
//        newRouter.rule().handler(logHandler).next();
//
//        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
//                .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
//                .handler(this.kfSessionHandler)
//                .end();
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
//                .handler(this.kfSessionHandler).end();
//
//        // 门店审核事件
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
//                .handler(this.storeCheckNotifyHandler).end();
//
//        // 自定义菜单事件
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxConsts.MenuButtonType.CLICK).handler(this.menuHandler).end();
//
//        // 点击菜单连接事件
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxConsts.MenuButtonType.VIEW).handler(this.nullHandler).end();
//

//
//        // 取消关注事件
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxConsts.EventType.UNSUBSCRIBE)
//                .handler(this.unsubscribeHandler).end();
//
//        // 上报地理位置事件
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxConsts.EventType.LOCATION).handler(this.locationHandler)
//                .end();
//
//        // 接收地理位置消息
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION)
//                .handler(this.locationHandler).end();
//
//        // 扫码事件
//        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
//                .event(WxConsts.EventType.SCAN).handler(this.scanHandler).end();
//
//        // 默认
//        newRouter.rule().async(false).handler(this.msgHandler).end();

//    }


}
