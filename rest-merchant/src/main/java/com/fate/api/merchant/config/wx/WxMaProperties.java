package com.fate.api.merchant.config.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: parent
 * @description: 微信小程序商户端配置
 * @author: chenyixin
 * @create: 2019-07-17 16:44
 **/
@Data
@ConfigurationProperties(prefix = "wx.applet")
public class WxMaProperties {
    /**
     * 设置微信小程序的appid
     */
    private String appId;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 设置微信小程序消息服务器配置的token
     */
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;
}
