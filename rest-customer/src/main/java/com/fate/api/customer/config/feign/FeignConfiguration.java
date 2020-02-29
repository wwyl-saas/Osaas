package com.fate.api.customer.config.feign;

import com.fate.api.customer.service.MerchantSettleService;
import com.fate.common.util.CurrentMerchantUtil;
import com.google.common.collect.Maps;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @program: parent
 * @description: 远程接口调用配置
 * @author: chenyixin
 * @create: 2019-09-13 00:36
 **/
@Configuration
public class FeignConfiguration {
    private static final Map<String, MerchantSettleService> settleServices = Maps.newHashMap();
    @Value("${feign.merchant.ips}")
    private String merchantIps;
    @Value("${feign.merchant.port}")
    private String merchantPort;
    @Value("${feign.merchant.domain}")
    private String merchantDomain;
//    /**
//     * 商户端接口
//     * @return
//     */
//    @Bean
//    public MerchantRestService merchantRestService(){
//        Decoder decoder = new GsonDecoder();
//        Encoder encoder = new GsonEncoder();
//        return Feign.builder()
//                .encoder(encoder)
//                .decoder(decoder)
//                .logger(new Logger.ErrorLogger())
//                .logLevel(Logger.Level.HEADERS)
//                .requestInterceptor(template -> {
//                    Long merchantId=CurrentMerchantUtil.getMerchant().getId();
//                    template.header("merchantId", String.valueOf(merchantId));
//                })
//                .target(MerchantRestService.class, merchantDomain);//域名调用自动负载均衡，不需要ribbon
//    }

    /**
     * 商户端接口
     *
     * @return
     */
    @PostConstruct
    public void cusomerSettleRestService() {
        Decoder decoder = new GsonDecoder();
        Encoder encoder = new GsonEncoder();

        Feign.Builder builder = Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
//                .errorDecoder(new GitHubErrorDecoder(decoder))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.HEADERS)
                .requestInterceptor(template -> {
                    Long merchantId=CurrentMerchantUtil.getMerchant().getId();
                    template.header("merchantId", String.valueOf(merchantId));
                });

        String[] ips = merchantIps.split(",");
        for (String ip : ips) {
            String domain = new StringBuilder("http://").append(ip).append(":").append(merchantPort).toString();//内网http协议
            MerchantSettleService settleService = builder.target(MerchantSettleService.class, domain);
            settleServices.put(ip,settleService);
        }
    }

    /**
     * 返回对应服务器的service
     * @param ip
     * @return
     */
    public static MerchantSettleService getMerchantSettleService(String ip){
        MerchantSettleService merchantSettleService=settleServices.get(ip);
        Assert.notNull(merchantSettleService,"customerService不存在");
        return merchantSettleService;
    }
}
