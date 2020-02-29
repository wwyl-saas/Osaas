package com.fate.api.merchant.config.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: parent
 * @description: 企业微信公众号配置
 * @author: chenyixin
 * @create: 2019-07-17 17:13
 **/
@Data
@ConfigurationProperties(prefix = "wx.public")
public class WxMpProperties {
    /**
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 设置微信公众号的app secret
     */
    private String secret;

    /**
     * 设置微信公众号的token
     */
    private String token;

    /**
     * 设置微信公众号的EncodingAESKey
     */
    private String aesKey;
}
