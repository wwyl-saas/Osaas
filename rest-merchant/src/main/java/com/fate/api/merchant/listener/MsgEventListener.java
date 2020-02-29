package com.fate.api.merchant.listener;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import com.fate.api.merchant.event.*;
import com.fate.common.util.SubmailSmsUtil;
import com.fate.common.util.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @program: parent
 * @description: 消息事件监听器
 * @author: chenyixin
 * @create: 2019-06-14 19:43
 **/
@Component
@Slf4j
public class MsgEventListener {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxMaService wxMaService;

    @Value("${cdn.domain}")
    private String cdnDomain;
    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 邮件事件监听器
     *
     * @param event
     */
    @EventListener
    public void mailListener(MailMsgEvent event) {
        try {
            log.info("发送邮件,event={}",event.toString());
            if (!event.isHtml() && MapUtils.isEmpty(event.getAttachments())) {
                //简单邮件
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(sender);
                simpleMailMessage.setTo(event.getTo());
                simpleMailMessage.setSubject(event.getSubject());
                simpleMailMessage.setText(event.getContent());
                javaMailSender.send(simpleMailMessage);
            } else {
                //html或者附件邮件
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setFrom(sender);
                mimeMessageHelper.setTo(event.getTo());
                mimeMessageHelper.setSubject(event.getSubject());
                mimeMessageHelper.setText(event.getContent(), event.isHtml());
                //添加行内图片
                if (MapUtils.isNotEmpty(event.getHtmlImages())) {
                    for (Map.Entry<String, Resource> entry : event.getHtmlImages().entrySet()) {
                        mimeMessageHelper.addInline(entry.getKey(), entry.getValue());
                    }
                }
                //添加附件
                if (MapUtils.isNotEmpty(event.getAttachments())) {
                    for (Map.Entry<String, InputStreamSource> entry : event.getAttachments().entrySet()) {
                        mimeMessageHelper.addAttachment(entry.getKey(), entry.getValue());
                    }
                }
                javaMailSender.send(mimeMessage);
            }
        } catch (Exception e) {
            log.error("{}发送消息失败", event, e);
        }
    }


    /**
     * 短信事件监听器
     *
     * @param event
     */
    @EventListener
    public void smsListener(ShortMsgEvent event) {
        try {
            log.info("发送短信消息,event={}",event);
            String content= TemplateUtil.render(event.getTemplate(),event.getParams());
            for (String phone : event.getPhones()) {
                SubmailSmsUtil.sendSafeSms(phone,content);
            }
        } catch (Exception e) {
            log.error("发送短信报错",e);
        }
    }

    /**
     * 公众号消息事件监听器
     *
     * @param event
     */
    @EventListener
    public void wxPublicListener(PublicMsgEvent event) {
        try {
            log.info("发送公众号消息,event={}",event);
            event.getOpenIds().stream().forEach(openId -> {
                //推送消息
                WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                        .toUser(openId)
                        .templateId(event.getTemplateId())
                        .data(event.getTemplateData())
                        .url(event.getUrl())//点击模版消息要访问的网址
                        .miniProgram(event.getMiniProgram())//点击模版跳转到小程序页面，优先
                        .build();

                try {
                    wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
                } catch (WxErrorException e) {
                    log.error("{}发送用户{}微信公众号消息失败", event, openId, e);
                }
            });

        } catch (Exception e) {
            log.error("{}发送消息失败", event, e);
        }
    }


    /**
     * 小程序消息事件监听器
     *
     * @param event
     */
    @EventListener
    public void wxAppletListener(WxAppletMsgEvent event) {
        try {
            log.info("发送小程序消息,event={}",event);
            //设置推送消息
            WxMaTemplateMessage templateMessage = WxMaTemplateMessage.builder()
                    .toUser(event.getOpenId())
                    .formId(event.getFormId())
                    .templateId(event.getTemplateId())
                    .data(event.getTemplateData())
                    .page(event.getPage())
                    .emphasisKeyword(event.getEmphasisKeyword())//模板需要放大的关键词，不填则默认无放大
                    .build();

            wxMaService.getMsgService().sendTemplateMsg(templateMessage);
            log.info("小程序消息发送成功");
        } catch (WxErrorException e) {
            log.error("{}发送微信小程序消息失败", event, e);
        } catch (Exception e) {
            log.error("{}发送消息失败", event, e);
        }
    }

    /**
     * 关联小程序的公众号统一消息事件监听器
     * 下发小程序和公众号统一的服务消息
     *
     * @param event
     */
    @EventListener
    public void wxUnionPublicListener(WxUnionPublicMsgEvent event) {
        try {
            log.info("发送公众号消息,event={}",event);
            if (event.isMpTemplateMsg()) {
                event.getOpenIds().stream().map(openId -> {
                    WxMaUniformMessage message = WxMaUniformMessage.builder()
                            .isMpTemplateMsg(event.isMpTemplateMsg())
                            .toUser(openId)
                            .appid(event.getPublicAppid())//公众号appid，要求与小程序有绑定且同主体.
                            .templateId(event.getTemplateId())
                            .url(event.getUrl())
                            .miniProgram(event.getMiniProgram())//公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系.
                            .data(event.getData())
                            .build();
                    return message;
                }).forEach(message -> {
                    try {
                        wxMaService.getMsgService().sendUniformMsg(message);
                    } catch (WxErrorException e) {
                        log.error("{}发送用户{}微信公众号消息失败", event, message.getToUser(), e);
                    }
                });
            } else {
                WxMaUniformMessage message = WxMaUniformMessage.builder()
                        .isMpTemplateMsg(event.isMpTemplateMsg())
                        .toUser(event.getOpenIds().get(0))
                        .templateId(event.getTemplateId())
                        .page(event.getPage())
                        .formId(event.getFormId())
                        .emphasisKeyword(event.getEmphasisKeyword())
                        .data(event.getData())
                        .build();
                try {
                    wxMaService.getMsgService().sendUniformMsg(message);
                } catch (WxErrorException e) {
                    log.error("{}发送用户{}微信小程序消息失败", event, message.getToUser(), e);
                }
            }

        } catch (Exception e) {
            log.error("{}发送消息失败", event, e);
        }
    }

}
