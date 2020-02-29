package com.fate.api.customer.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import com.fate.api.customer.config.wx.WxMaConfiguration;
import com.fate.api.customer.util.CurrentApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Objects;

/**
 * @program: parent
 * @description: 微信小程序客服回调接口
 * @author: chenyixin
 * @create: 2019-05-30 00:10
 **/
@ApiIgnore//使用该注解忽略这个API
@RestController
@RequestMapping("/callback/{applicationId}/v1/applet/massage")//前两位位置固定
@Slf4j
public class AppletKeFuCallbackController {


    /**
     * 微信客服消息认证接口
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable String applicationId,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]", signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(CurrentApplicationUtil.getMerchantApplication().getAppId());
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }else {
            return "非法请求";
        }
    }

    /**
     * 微信客服消息接收接口
     * @param requestBody
     * @param msgSignature
     * @param encryptType
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String applicationId,
                       @RequestBody String requestBody,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam(name = "encrypt_type", required = false) String encryptType,
                       @RequestParam(name = "signature", required = false) String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        log.info("接收微信请求：[msg_signature=[{}], encrypt_type=[{}], signature=[{}]," +
                " timestamp=[{}], nonce=[{}], requestBody=[{}] ", msgSignature, encryptType, signature, timestamp, nonce, requestBody);

        String appid=CurrentApplicationUtil.getMerchantApplication().getAppId();
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        final boolean isJson = Objects.equals(wxService.getWxMaConfig().getMsgDataFormat(), WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encryptType)) { // 明文传输的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(requestBody);
            } else {//xml
                inMessage = WxMaMessage.fromXml(requestBody);
            }
            try {
                WxMaConfiguration.getMessageRouter(appid).route(inMessage);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return "success";
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(requestBody, wxService.getWxMaConfig());
            } else {//xml
                inMessage = WxMaMessage.fromEncryptedXml(requestBody, wxService.getWxMaConfig(), timestamp, nonce, msgSignature);
            }

            try {
                WxMaConfiguration.getMessageRouter(appid).route(inMessage);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return "success";
        }

        throw new RuntimeException("不可识别的加密类型：" + encryptType);
    }
}
