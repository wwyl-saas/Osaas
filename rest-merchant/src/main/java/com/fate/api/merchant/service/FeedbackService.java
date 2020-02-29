package com.fate.api.merchant.service;

import com.fate.api.merchant.dto.FeedbackDto;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.FeedbackDao;
import com.fate.common.dao.FeedbackUserDao;
import com.fate.common.entity.*;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 反馈
 * @author: chenyixin
 * @create: 2019-09-13 00:59
 **/
@Service
@Slf4j
public class FeedbackService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    FeedbackDao feedbackDao;
    @Resource
    FeedbackUserDao feedbackUserDao;

    /**
     * 获取反馈详情
     * @param feedbackId
     * @return
     */
    public FeedbackDto getDetail(Long feedbackId) {
        Feedback feedback=feedbackDao.getById(feedbackId);
        Assert.notNull(feedback,"反馈不存在");
        FeedbackUser feedbackUser=feedbackUserDao.getByUserIdAndFeedbackId(CurrentUserUtil.getMerchantUser().getId(),feedbackId);
        FeedbackDto result= BeanUtil.mapper(feedback,FeedbackDto.class);
        if (feedbackUser!=null)
        result.setIsRead(feedbackUser.getIsRead());
        if (!org.springframework.util.StringUtils.isEmpty(feedback.getPictureUrls())){
            List<String> pictureList= Arrays.stream(feedback.getPictureUrls().split(",")).map(url->cdnDomain+url).collect(Collectors.toList());
            result.setPictureList(pictureList);
        }
        return result;
    }


    /**
     * 获取对某个商户的反馈列表
     * @return
     */
    public List<FeedbackDto> getFeedbackListByMerchant() {
        List<FeedbackDto> result= Lists.emptyList();
        MerchantUser user= CurrentUserUtil.getMerchantUser();
        List<Feedback> feedbacks=feedbackDao.list();
        List<FeedbackUser> feedbackUsers=feedbackUserDao.list();
        if (CollectionUtils.isNotEmpty(feedbacks)){
            List<Long> feedbackIds=feedbacks.stream().map(feedback-> feedback.getId()).collect(Collectors.toList());
           // Map<Long, Boolean> isReadMap=feedbackUsers.stream().collect(Collectors.toMap(feedbackUser->feedbackUser.getFeekbackId(),feedbackUser->feedbackUser.getIsRead()));
            //反馈列表
            if (CollectionUtils.isNotEmpty(feedbacks)){
                result=feedbacks.stream().map(feedback -> {
                    FeedbackDto dto=BeanUtil.mapper(feedback,FeedbackDto.class);
                    //dto.setIsRead(isReadMap.get(feedback.getId()));//数据问题
                    if (!StringUtils.isEmpty(feedback.getPictureUrls())){
                        List<String> pictureList= Arrays.stream(feedback.getPictureUrls().split(",")).map(url->cdnDomain+url).collect(Collectors.toList());
                        dto.setPictureList(pictureList);
                    }
                    return dto;
                }).collect(Collectors.toList());
            }
        }
        return result;
    }




}
