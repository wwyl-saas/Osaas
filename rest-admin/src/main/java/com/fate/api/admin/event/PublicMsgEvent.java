package com.fate.api.admin.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @program: parent
 * @description: 公众号通知
 * @author: chenyixin
 * @create: 2019-06-14 19:36
 **/
@Getter
@Setter
@Accessors(chain = true)
public class PublicMsgEvent extends ApplicationEvent {
    /**
     * 推送消息的微信公众号ID
     */
    private String appid;
    /**
     * 消息接收人openID（必须是对应appid下的Openid）
     */
    private List<String> openIds;
    /**
     * 对应appid应用设置的模板ID
     */
    private String templateId;
    /**
     * 对应模板的数据
     */
    private List<WxMpTemplateData> templateData;
    /**
     * 消息点击跳转URL
     */
    private String url;
    /**
     * 消息点击跳转小程序，不需跳小程序可不用传该数据，都传时优先跳转小程序
     */
    private WxMpTemplateMessage.MiniProgram miniProgram;


    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public PublicMsgEvent(Object source) {
        super(source);
    }
}
