package com.fate.api.customer.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * @program: parent
 * @description: 邮件通知
 * @author: chenyixin
 * @create: 2019-06-14 19:34
 **/
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class MailMsgEvent extends ApplicationEvent {
    /**
     * 邮件接收人列表
     */
    private String[] to;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 是否为html邮件
     */
    private boolean html;
    /**
     * 图片 html中的cid(Content ID)-->图片资源
     */
    private Map<String,Resource> htmlImages;
    /**
     * 附件 附件名称-->附件资源
     */
    private Map<String,InputStreamSource> attachments;


    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MailMsgEvent(Object source) {
        super(source);
    }
}
