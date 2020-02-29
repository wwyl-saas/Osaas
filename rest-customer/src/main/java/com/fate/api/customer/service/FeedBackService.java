package com.fate.api.customer.service;

import com.fate.api.customer.query.FeedbackCreateQuery;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.Feedback;
import com.fate.common.enums.MessageTag;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @program: parent
 * @description: 用户意见反馈
 * @author: songjignhuan
 * @create: 2019-05-23 08:15
 **/
@Service
@Slf4j
public class FeedBackService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MqSendService mqSendService;


    /**
     * 创建反馈信息
     *
     * @param feedbackCreateQuery
     */
    public void createFeedback(FeedbackCreateQuery feedbackCreateQuery) {
        Customer customer=CurrentCustomerUtil.getCustomer();
        Feedback feekback = new Feedback()
                .setMerchantAppId(CurrentApplicationUtil.getMerchantApplication().getId())
                .setPictureUrls(feedbackCreateQuery.getPictureUrls())
                .setCustomerId(CurrentCustomerUtil.getCustomer().getId())
                .setRemark(feedbackCreateQuery.getRemark())
                .setType(feedbackCreateQuery.getType())
                .setAnonymous(feedbackCreateQuery.getAnonymous());

        if (feedbackCreateQuery.getAnonymous()){
            feekback.setCustomerName("用户");
            feekback.setCustomerAvatar(cdnDomain+"portraitdefault.png");
        }else {
            feekback.setCustomerName(StringUtils.isEmpty(customer.getName())?customer.getNickname():customer.getName());
            feekback.setCustomerAvatar(customer.getAvatar());
        }

        Assert.isTrue( feekback.insert(), "插入反馈信息失败");

        //通知商户
        MyMessage myMessage=new MyMessage(feekback.getId());
        MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.FEEDBACK_CREATE.getTag()).build();
        mqSendService.sendToMerchant(myMessage,mqProperties);
    }


}
