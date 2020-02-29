package com.fate.api.customer.event;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @program: parent
 * @description: 微信小程序和公众号统一消息下的公众号消息事件
 * @author: chenyixin
 * @create: 2019-06-18 00:07
 **/
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class WxUnionPublicMsgEvent extends ApplicationEvent {
    /**
     * 推送消息的微信小程序ID
     */
    private String appid;
    /**
     * 是否发送公众号模版消息，否则发送小程序模版消息.
     */
    private boolean isMpTemplateMsg;
    /**
     * 用户openid.
     * 可以是小程序的openid，也可以是mp_template_msg.appid对应的公众号的openid
     */
    private List<String> openIds;
    /**
     * 公众号appid，要求与小程序有绑定且同主体.
     */
    private String publicAppid;

    /**
     * 公众号或小程序模板ID.
     */
    private String templateId;

    /**
     * 公众号模板消息所要跳转的url.
     */
    private String url;

    /**
     * 小程序页面路径.
     */
    private String page;

    /**
     * 小程序模板消息formid.
     */
    private String formId;

    /**
     * 公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系.
     */
    private WxMaUniformMessage.MiniProgram miniProgram;

    /**
     * 小程序模板数据.
     */
    private List<WxMaTemplateData> data;

    /**
     * 模板需要放大的关键词，不填则默认无放大.
     */
    private String emphasisKeyword;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public WxUnionPublicMsgEvent(Object source) {
        super(source);
    }
}
