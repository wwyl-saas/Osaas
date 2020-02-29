package com.fate.api.admin.config.wx;


import com.fate.api.admin.service.CacheService;
import com.fate.common.dao.MerchantApplicationDao;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ApplicationType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: parent
 * @description: 商户自己的微信公众号功能配置（此项目主要是处理接口调用）
 * @author: chenyixin
 * @create: 2019-05-29 17:10
 **/
@Configuration
@DependsOn({"mybatisSqlSessionFactoryBean"})
@Slf4j
public class WxMpConfiguration {
    private static final Map<String, WxMpMessageRouter> routers = Maps.newHashMap();
    private static final Map<String, WxMpService> mpServices = Maps.newHashMap();

    @Resource
    private MerchantApplicationDao merchantApplicationDao;
    @Autowired
    CacheService cacheService;
    @Autowired
    RedissonClient redissonClient;


    @PostConstruct
    public void init() {
        List<MerchantApplication> applications = merchantApplicationDao.findAllEnable(Arrays.asList(ApplicationType.WX_PUBLIC));
        Assert.notEmpty(applications, "配置数据不能为空");

        applications.stream()
                .forEach(a -> {
                    MyMpRedisConfigImpl configStorage = new MyMpRedisConfigImpl();
                    configStorage.setAppId(a.getAppId());//设置微信公众号的appid
                    configStorage.setSecret(a.getAppSecret());//设置微信公众号的app secret
                    configStorage.setToken(a.getMsgToken());//设置微信公众号的token
                    configStorage.setAesKey(a.getMsgAesKey());//设置微信公众号的EncodingAESKey
                    configStorage.setMpId(a.getAppId());
                    configStorage.setCacheService(cacheService);
                    configStorage.setRedissonClient(redissonClient);

                    final WxMpService service = new WxMpServiceImpl();
                    service.setWxMpConfigStorage(configStorage);

                    mpServices.put(a.getAppId(), service);
                    routers.put(a.getAppId(), this.newRouter(service));
                });

    }

    public static WxMpService getMpService(String appid) {
        WxMpService mpService = mpServices.get(appid);
        Assert.notNull(mpService, String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        return mpService;
    }

    public static WxMpMessageRouter getMessageRouter(String appid) {
        WxMpMessageRouter router = routers.get(appid);
        Assert.notNull(router, String.format("未找到对应appid=[%s]的Router，请核实！", appid));
        return router;
    }


    public WxMpMessageRouter newRouter(WxMpService service) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(service);
        return newRouter;
    }

}
