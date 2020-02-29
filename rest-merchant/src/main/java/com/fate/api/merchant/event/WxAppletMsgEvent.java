package com.fate.api.merchant.event;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @program: parent
 * @description: 商户端小程序通知
 *  1. 当用户在小程序内完成过支付行为，可允许开发者向用户在7天内推送有限条数的模板消息
 *  （1次支付可下发3条，多次支付下发条数独立，互相不影响）
 *  2. 当用户在小程序内发生过提交表单行为且该表单声明为要发模板消息的，开发者需要向用户
 *  提供服务时，可允许开发者向用户在7天内推送有限条数的模板消息（1次提交表单可下发1条，
 *  多次提交下发条数独立，相互不影响
 *
 * @author: chenyixin
 * @create: 2019-06-14 19:38
 **/
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class WxAppletMsgEvent extends ApplicationEvent {
    /**
     * 消息接收人openID（必须是对应appid下的Openid）
     */
    private String openId;
    /**
     * 对应Appid应用的表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     */
    private String formId;
    /**
     * 对应Appid应用模板消息的id
     */
    private String templateId;
    /**
     * 对应模板的数据
     */
    private List<WxMaTemplateData> templateData;
    /**
     * 消息点击跳转小程序页面
     */
    private String page;
    /**
     * 模板需要放大的关键词，不填则默认无放大.
     */
    private String emphasisKeyword;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public WxAppletMsgEvent(Object source) {
        super(source);
    }
}
