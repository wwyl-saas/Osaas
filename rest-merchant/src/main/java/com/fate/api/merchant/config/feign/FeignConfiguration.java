package com.fate.api.merchant.config.feign;

import com.fate.api.merchant.service.CustomerRestService;
import com.fate.api.merchant.service.CustomerSettleService;
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
    private static final Map<String, CustomerSettleService> settleServices = Maps.newHashMap();
    @Value("${feign.customer.ips}")
    private String customerIps;
    @Value("${feign.customer.port}")
    private String customerPort;
    @Value("${feign.customer.domain}")
    private String customerDomain;

    /**
     * 商户端接口
     *
     * @return
     */
    @Bean
    public CustomerRestService customerRestService() {
        Decoder decoder = new GsonDecoder();
        Encoder encoder = new GsonEncoder();
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
//                .errorDecoder(new GitHubErrorDecoder(decoder))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.HEADERS)
//                .requestInterceptor(template -> { })
                .target(CustomerRestService.class, customerDomain);//域名调用自动负载均衡，不需要ribbon(外网效率低点)
    }


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
                .logLevel(Logger.Level.HEADERS);

        String[] ips = customerIps.split(",");
        for (String ip : ips) {
            String domain = new StringBuilder("http://").append(ip).append(":").append(customerPort).toString();
            CustomerSettleService settleService = builder.target(CustomerSettleService.class, domain);
            settleServices.put(ip,settleService);
        }
    }

    /**
     * 返回对应服务器的service
     * @param ip
     * @return
     */
    public static CustomerSettleService getCustomerSettleService(String ip){
        CustomerSettleService customerSettleService=settleServices.get(ip);
        Assert.notNull(customerSettleService,"customerService不存在");
        return customerSettleService;
    }


}
