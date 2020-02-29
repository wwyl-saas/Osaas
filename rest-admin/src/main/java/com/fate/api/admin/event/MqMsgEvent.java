package com.fate.api.admin.event;

import com.fate.common.util.CurrentMerchantUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @program: parent
 * @description: 消息总线推送事件
 * @author: chenyixin
 * @create: 2019-07-22 09:03
 **/
@Getter
@Setter
public class MqMsgEvent extends ApplicationEvent {
    /**
     * 商户Id
     */
    private String key= CurrentMerchantUtil.getMerchant().getId().toString();
    /**
     * 主题
     */
    private String topic;
    /**
     * 标签
     */
    private String tag;
    /**
     * 消息体
     */
    private String jsonData;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MqMsgEvent(Object source) {
        super(source);
    }
}
