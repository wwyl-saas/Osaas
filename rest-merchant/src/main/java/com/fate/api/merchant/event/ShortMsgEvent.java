package com.fate.api.merchant.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;
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
@Slf4j
@ToString
public class ShortMsgEvent extends ApplicationEvent {
    /**
     * 接收人手机号
     */
    private String[] phones;
    /**
     * 短信模板id
     */
    private String template;
    /**
     * 短信模板参数
     */
    private Map<String,Object> params;

    /** 签名**/
    public final String smsSign = "万物优联";

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ShortMsgEvent(Object source) {
        super(source);
    }
}
