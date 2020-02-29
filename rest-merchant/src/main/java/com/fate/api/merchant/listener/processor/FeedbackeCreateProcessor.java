package com.fate.api.merchant.listener.processor;

import com.fate.api.merchant.cons.MailTemplate;
import com.fate.api.merchant.cons.PublicTemplate;
import com.fate.api.merchant.event.MailMsgEvent;
import com.fate.api.merchant.event.PublicMsgEvent;
import com.fate.api.merchant.service.CustomerService;
import com.fate.api.merchant.service.MerchantService;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.common.cons.Const;
import com.fate.common.dao.FeedbackDao;
import com.fate.common.dao.FeedbackUserDao;
import com.fate.common.dao.MerchantApplicationDao;
import com.fate.common.entity.*;
import com.fate.common.enums.FeedBackType;
import com.fate.common.model.MyMessage;
import com.fate.common.util.DateUtil;
import com.fate.common.util.QiNiuUtil;
import com.fate.common.util.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-23 17:36
 **/

@Component
@Slf4j
public class FeedbackeCreateProcessor implements MessageProcessor<MyMessage>{
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Value("${wx.applet.appId}")
    private String appletId;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    CustomerService customerService;
    @Autowired
    MerchantService merchantService;
    @Resource
    FeedbackDao feedbackDao;
    @Resource
    FeedbackUserDao feedbackUserDao;
    @Resource
    MerchantApplicationDao merchantApplicationDao;
    @Autowired
    MerchantUserService merchantUserService;

    @Override
    public boolean handle(MyMessage message) {
        Feedback feedback=feedbackDao.getById(message.getId());
        Assert.notNull(feedback,"反馈不存在");
        Customer customer = customerService.getById(feedback.getCustomerId());
        MerchantApplication application=merchantApplicationDao.getById(feedback.getMerchantAppId());

        //应用问题邮件通知企业
        if (feedback.getType().equals(FeedBackType.APPLICATION_PROBLEM)) {
            MailMsgEvent mailEvent = new MailMsgEvent(this);
            mailEvent.setHtml(true);
            mailEvent.setTo(Const.admins);
            mailEvent.setSubject("用户反馈消息");

            String pictures = feedback.getPictureUrls();
            if (StringUtils.isNotBlank(pictures)) {
                String[] pics = pictures.split(",");
                Map<String, org.springframework.core.io.Resource> htmlImages = new HashMap<>();
                for (int i = 1; i <= pics.length; i++) {
                    htmlImages.put("cid" + i, new FileSystemResource(QiNiuUtil.downLoadToFile(cdnDomain + pics[i - 1], false)));
                }
                mailEvent.setHtmlImages(htmlImages);
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("applicationName", application.getName());
            paramMap.put("applicationId", application.getId().toString());
            paramMap.put("customerName", customer.getName());
            paramMap.put("customerPhone", customer.getMobile());
            paramMap.put("remark", feedback.getRemark());
            if (MapUtils.isNotEmpty(mailEvent.getHtmlImages()))
                paramMap.put("pics", mailEvent.getHtmlImages().keySet());
            String content = TemplateUtil.render(MailTemplate.FEEDBACK_TEMPLATE, paramMap);
            mailEvent.setContent(content);

            applicationContext.publishEvent(mailEvent);
        } else {//其他问题通知商户后台

            PublicMsgEvent wxPublicEvent = new PublicMsgEvent(this);
            wxPublicEvent.setTemplateId(PublicTemplate.FEEDBACK_TEMPLATE_ID);
            List<MerchantUser> merchantUsers = merchantUserService.getFeedbackNoticeUser();
            if (CollectionUtils.isNotEmpty(merchantUsers)) {
                List<FeedbackUser> feedbackUsers= merchantUsers.stream().map(merchantUser -> new FeedbackUser().setMerchantUserId(merchantUser.getId())
                        .setFeekbackId(feedback.getId()).setIsRead(false)).collect(Collectors.toList());
                feedbackUserDao.saveBatch(feedbackUsers);

                List<String> openIds = merchantUsers.stream().filter(merchantUser -> StringUtils.isNotBlank(merchantUser.getWxMpOpenid())).map(merchantUser -> merchantUser.getWxMpOpenid()).collect(Collectors.toList());
                wxPublicEvent.setOpenIds(openIds);
            }

            List<WxMpTemplateData> templateData = new ArrayList<>();
            String customerName = "匿名用户";
            if (feedback.getAnonymous()) {
                customerName = customer.getName();
            }

            templateData.add(new WxMpTemplateData("first", "刚刚有客户提交了一个反馈"));
            templateData.add(new WxMpTemplateData("keyword1", customerName));
            templateData.add(new WxMpTemplateData("keyword2", DateUtil.getLocalDateTimeString(feedback.getCreateTime())));
            templateData.add(new WxMpTemplateData("keyword3", feedback.getRemark()));
            templateData.add(new WxMpTemplateData("remark", "查看详情请点击下方进入商户端小程序"));
            wxPublicEvent.setTemplateData(templateData);
            WxMpTemplateMessage.MiniProgram miniProgram=new WxMpTemplateMessage.MiniProgram();
            miniProgram.setAppid(appletId);
            miniProgram.setPagePath("pages/notice/feedback?feedbackId="+feedback.getId());
            wxPublicEvent.setMiniProgram(miniProgram);
            applicationContext.publishEvent(wxPublicEvent);
        }
        return false;
    }
}
