package com.fate.api.admin.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * @program: parent
 * @description: 短信通知
 * @author: chenyixin
 * @create: 2019-06-14 19:35
 **/
@Getter
@Setter
@Accessors(chain = true)
public class ShortMsgEvent extends ApplicationEvent {
    /**
     * 接收人手机号
     */
    private String[] phones;
    /**
     * 短信模板id
     */
    private String templateId;
    /**
     * 短信模板数据
     */
    private Map<String,String> templateDate;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ShortMsgEvent(Object source) {
        super(source);
    }
}
