package com.fate.api.admin.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @program: parent
 * @description: 微信开放平台回调
 * @author: chenyixin
 * @create: 2019-10-21 10:50
 **/
@ApiIgnore//使用该注解忽略这个API
@RestController
@RequestMapping("/open/callback")
@Slf4j
public class WxOpenCallbackController {
    @Autowired
    WxOpenService wxOpenService;
    @Autowired
    private WxOpenMessageRouter wxOpenMessageRouter;


    /**
     *    用于接收取消授权通知、授权成功通知、授权更新通知，也用于接收验证票据（component_verify_ticket），
     *   component_verify_ticket 是验证平台方的重要凭据，请妥善保存。
     *   服务方在获取 component_access_token 时需要提供最新推送的 component_verify_ticket 以供验证身份合法性
     * @param requestBody
     * @param timestamp
     * @param nonce
     * @param signature
     * @param encType
     * @param msgSignature
     * @return
     */
    @RequestMapping("/massage/ticket")
    public Object receiveTicket(@RequestBody(required = false) String requestBody, @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce, @RequestParam("signature") String signature,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        this.log.info("接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ", signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!StringUtils.equalsIgnoreCase("aes", encType) || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        // aes加密的消息
        WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        this.log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        try {
            String out = wxOpenService.getWxOpenComponentService().route(inMessage);
            this.log.debug("\n组装回复信息：{}", out);
        } catch (WxErrorException e) {
            this.log.error("receive_ticket", e);
        }
        return "success";
    }


    /**
     * 该 URL 用于接收已授权公众号的消息和事件，消息内容、消息格式、签名方式、加密方式与普通公众号接收的一致，唯一区别在于签名 token 和加密 symmetric_key 使用的是
     * 服务方申请时所填写的信息。由于消息具体内容不会变更，故根据消息内容里的 ToUserName，服务方是可以区分出具体消息所属的公众号。请注意：
     *
     * 1、考虑到服务需要接收大量的授权公众号的消息，为了便于做业务分流和业务隔离，必须提供如下形式的 url：www.abc.com/aaa/$APPID$/bbb/cgi，
     * 其中$APPID$在实际推送时会替换成所属的已授权公众号的 appid。
     *
     * 2、第三方平台只需获得某个业务模块的授权（而不需要获得客服与菜单权限的授权），然后在收到该业务模块事件推送后，如果该事件是允许进行 5 秒内被动回复消息给粉丝
     * 的，那么第三方就可以被动回复（业务模块的哪些事件推送允许被动回复用户，哪些不允许，需咨询具体业务模块），点击查看被动回复接口文档
     *
     * 3、如果第三方希望实现实时获知公众号有新粉丝关注，只需要收到关注事件后回复 success 即可，不必另行回复，免得公众号出现多个第三方同时进行粉丝关注后的自动回复。
     * @param requestBody
     * @param appId
     * @param signature
     * @param timestamp
     * @param nonce
     * @param openid
     * @param encType
     * @param msgSignature
     * @return
     */
    @RequestMapping("/massage/{appId}")
    public Object callback(@RequestBody(required = false) String requestBody,
                           @PathVariable("appId") String appId,
                           @RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("openid") String openid,
                           @RequestParam("encrypt_type") String encType,
                           @RequestParam("msg_signature") String msgSignature) {
        this.log.info("接收微信请求：[appId=[{}], openid=[{}], signature=[{}], encType=[{}], msgSignature=[{}],timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                appId, openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
        if (!StringUtils.equalsIgnoreCase("aes", encType) || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = "";
        // aes加密的消息
        WxMpXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedMpXml(requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        this.log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        // 全网发布测试用例
        if (StringUtils.equalsAnyIgnoreCase(appId, "wxd101a85aa106f53e", "wx570bc396a51b8ff8")) {
            try {
                if (StringUtils.equals(inMessage.getMsgType(), "text")) {
                    if (StringUtils.equals(inMessage.getContent(), "TESTCOMPONENT_MSG_TYPE_TEXT")) {
                        out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(
                                WxMpXmlOutMessage.TEXT().content("TESTCOMPONENT_MSG_TYPE_TEXT_callback")
                                        .fromUser(inMessage.getToUser())
                                        .toUser(inMessage.getFromUser())
                                        .build(),
                                wxOpenService.getWxOpenConfigStorage()
                        );
                    } else if (StringUtils.startsWith(inMessage.getContent(), "QUERY_AUTH_CODE:")) {
                        String msg = inMessage.getContent().replace("QUERY_AUTH_CODE:", "") + "_from_api";
                        WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT().content(msg).toUser(inMessage.getFromUser()).build();
                        wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService().sendKefuMessage(kefuMessage);
                    }
                } else if (StringUtils.equals(inMessage.getMsgType(), "event")) {
                    WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT().content(inMessage.getEvent() + "from_callback").toUser(inMessage.getFromUser()).build();
                    wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService().sendKefuMessage(kefuMessage);
                }
            } catch (WxErrorException e) {
                log.error("callback", e);
            }
        }else{
            WxMpXmlOutMessage outMessage = wxOpenMessageRouter.route(inMessage, appId);
            if(outMessage != null){
                out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(outMessage, wxOpenService.getWxOpenConfigStorage());
            }
        }
        return out;
    }


}
