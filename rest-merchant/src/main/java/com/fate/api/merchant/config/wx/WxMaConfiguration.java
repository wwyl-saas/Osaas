package com.fate.api.merchant.config.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import com.fate.api.merchant.service.CacheService;
import com.fate.common.dao.MerchantApplicationDao;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ApplicationType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
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
 * @description: 所有商户的C端小程序（后台部分数据接口需要，不做微信回调信息处理）
 * @author: chenyixin
 * @create: 2019-09-06 22:22
 **/
@Configuration
@DependsOn({"mybatisSqlSessionFactoryBean"})
@Slf4j
public class WxMaConfiguration {
    @Resource
    private MerchantApplicationDao merchantApplicationDao;
    @Autowired
    CacheService cacheService;
    @Autowired
    RedissonClient redissonClient;

    private static final Map<String, WxMaService> maServices = Maps.newHashMap();
    @PostConstruct
    public void init() {
        List<MerchantApplication> applications = merchantApplicationDao.findAllEnable(Arrays.asList(ApplicationType.WX_APPLET));
        Assert.notEmpty(applications, "配置数据不能为空");
        applications.stream()
                .forEach(a -> {
                    MyMaRedisConfigImpl config = new MyMaRedisConfigImpl();
                    config.setAppid(a.getAppId());//设置微信小程序的appid
                    config.setSecret(a.getAppSecret());//设置微信小程序的Secret
                    config.setToken(a.getMsgToken());//设置微信小程序消息服务器配置的token
                    config.setAesKey(a.getMsgAesKey());//设置微信小程序消息服务器配置的EncodingAESKey
                    config.setMsgDataFormat(a.getMsgFormatType().getDesc());//消息格式，XML或者JSON
                    config.setMaId(a.getAppId());
                    config.setCacheService(cacheService);
                    config.setRedissonClient(redissonClient);

                    final WxMaService service = new WxMaServiceImpl();
                    service.setWxMaConfig(config);
                    maServices.put(a.getAppId(), service);
                });
    }

    public static WxMaService getMaService(String appid) {
        WxMaService wxService = maServices.get(appid);
        Assert.notNull(wxService, String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        return wxService;
    }
}
