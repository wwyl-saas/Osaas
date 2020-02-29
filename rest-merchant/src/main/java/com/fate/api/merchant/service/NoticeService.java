package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.query.NoticeCreateQuery;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.*;
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
 * @description: 通知
 * @author:
 * @create: 2019-08-30 13:28
 **/
@Service
@Slf4j
public class NoticeService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    AnnounceUserDao announceUserDao;
    @Resource
    AnnounceDao announceDao;
    @Resource
    NoticeDao noticeDao;
    @Resource
    FeedbackUserDao feedbackUserDao;
    @Resource
    FeedbackDao feedbackDao;

    @Resource
    MerchantUserDao merchantUserDao;

    /**
     * 获取未读通知总数量
     * @return
     */
    public Integer getNoticeNumber() {
        MerchantUser user= CurrentUserUtil.getMerchantUser();
        //未读通知的数量
        Integer noticeCount=noticeDao.getUnreadNoticeCount(user.getId());
        //已读公告的数量
        Integer announceReadedCount=announceUserDao.getCountBy(user.getId());
        //所有公告数量
        Integer announceCount=announceDao.getCount();
        //未读反馈的数量
        Integer feedbackCount=feedbackUserDao.getUnreadCount(user.getId());
        return noticeCount+announceCount+feedbackCount-announceReadedCount;
    }

    /**
     * 获取未读通知数量
     * @return
     */
    public NoticeNumDto getNoticeNum() {
        MerchantUser user= CurrentUserUtil.getMerchantUser();
        //未读通知的数量
        Integer noticeCount=noticeDao.getUnreadNoticeCount(user.getId());
        //未读反馈的数量
        Integer feedbackCount=feedbackUserDao.getUnreadCount(user.getId());
        //已读公告的数量
        Integer announceReadedCount=announceUserDao.getCountBy(user.getId());
        //所有公告数量
        Integer announceCount=announceDao.getCount();
        return  NoticeNumDto.builder().noticeCount(noticeCount).announceCount(announceCount-announceReadedCount).feedbackCount(feedbackCount).build();
    }

    /**
     * 获取通知列表
     * @return
     */
    public List<NoticeDto> getNoticeList() {
        List<NoticeDto> result= Lists.emptyList();
        MerchantUser user= CurrentUserUtil.getMerchantUser();
        List<Notice> notices=noticeDao.getListByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(notices)){
            result=notices.stream().map(notice ->{
                NoticeDto noticeDto=BeanUtil.mapper(notice,NoticeDto.class);
                noticeDto.setAvatar(cdnDomain+noticeDto.getAvatar());
                return noticeDto;
            }).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取公告列表
     * @return
     */
    public List<AnnounceDto> getAnnounceList() {
        List<AnnounceDto> result= Lists.emptyList();
        MerchantUser user= CurrentUserUtil.getMerchantUser();
        List<Announce> announces=announceDao.getList();
        if (CollectionUtils.isNotEmpty(announces)){
            List<Long> announceIds=announces.stream().map(announce -> announce.getId()).collect(Collectors.toList());
            //用户已读列表
            List<AnnounceUser> announceUsers=announceUserDao.getListByUserIdAndAnnounceIds(user.getId(),announceIds);
            Map<Long, AnnounceUser> announceUserMap=new HashMap<>();
            if (CollectionUtils.isNotEmpty(announceUsers)){
                announceUsers.forEach(announceUser -> announceUserMap.put(announceUser.getAnnounceId(),announceUser));
            }
            result=announces.stream().map(announce -> {
                AnnounceDto dto=BeanUtil.mapper(announce,AnnounceDto.class);
                dto.setIsRead(announceUserMap.containsKey(announce.getId()));
                return dto;
            }).collect(Collectors.toList());
        }
        return result;
    }



    /**
     * 设置已读
     * @param noticeId
     */
    public void setNoticeRead(Long noticeId) {
        Notice notice=noticeDao.getById(noticeId);
        Assert.notNull(notice,"数据不存在");
        if (!notice.getIsRead()){
            notice.setIsRead(true);
            Assert.isTrue(notice.updateById(),"更新数据失败");
        }
    }

    /**
     * 设置已读
     * @param announceId
     */
    public void setAnnounceRead(Long announceId) {
        MerchantUser merchantUser=CurrentUserUtil.getMerchantUser();
        AnnounceUser announceUser=announceUserDao.getByUserIdAndAnnounceId(merchantUser.getId(),announceId);
        if (announceUser==null){
            AnnounceUser announceUserNew=new AnnounceUser().setAnnounceId(announceId).setMerchantUserId(merchantUser.getId());
            announceUserNew.insert();
        }
    }

    /**
     * 设置已读
     * @param feedbackId
     */
    public void setFeedbackRead(Long feedbackId) {
        FeedbackUser feedbackUser=feedbackUserDao.getByUserIdAndFeedbackId(CurrentUserUtil.getMerchantUser().getId(),feedbackId);
        Assert.notNull(feedbackUser,"数据不存在");
        if (!feedbackUser.getIsRead()){
            feedbackUser.setIsRead(true);
            Assert.isTrue(feedbackUser.updateById(),"更新数据失败");
        }
    }

    /**
     * 创建内部通知,发送给从界面选中的内部员工
     * @param noticeQuery
     */
    public void createNotice(NoticeCreateQuery noticeQuery) {

        //根据用户ID集合查询到用户的id，name，头像
        List<Long> userIdlist= Arrays.stream(noticeQuery.getMerchantUserIds().split(",")).map(id->Long.valueOf(id)).collect(Collectors.toList());
        List<MerchantUser> userList=merchantUserDao.getMerchantUserByIds(userIdlist);

        Map<Long, MerchantUser> userMap=new HashMap<Long, MerchantUser>();
        for(MerchantUser merchantUser:userList){
            userMap.put(merchantUser.getId(),merchantUser);
        }
        List<Notice> result=new ArrayList<Notice>();
        for(Long userId:userIdlist){
            Notice notice=new Notice();
            notice.setSubject(noticeQuery.getSubject());
            notice.setContent(noticeQuery.getContent());
            notice.setType(noticeQuery.getType());//通知类型有哪些
            notice.setIsRead(false);
            notice.setMerchantUserId(userId);
            notice.setName(userMap.get(userId).getName());
            notice.setAvatar(userMap.get(userId).getAvatarUrl());
            result.add(notice);
        }
        if(CollectionUtils.isNotEmpty(result)){
            noticeDao.saveBatch(result);
        }
    }

    /**
     * 获取反馈列表
     * @return
     */
    public List<FeedbackDto> getFeedbackList() {
        List<FeedbackDto> result= Lists.emptyList();
        MerchantUser user= CurrentUserUtil.getMerchantUser();
        List<FeedbackUser> feedbackUsers=feedbackUserDao.getListByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(feedbackUsers)){
            List<Long> feedbackIds=feedbackUsers.stream().map(feedbackUser -> feedbackUser.getFeekbackId()).collect(Collectors.toList());
            Map<Long, Boolean> isReadMap=feedbackUsers.stream().collect(Collectors.toMap(feedbackUser->feedbackUser.getFeekbackId(),feedbackUser->feedbackUser.getIsRead()));
            //反馈列表
            Collection<Feedback> feedbacks=feedbackDao.listByIds(feedbackIds);
            if (CollectionUtils.isNotEmpty(feedbacks)){
                result=feedbacks.stream().map(feedback -> {
                    FeedbackDto dto=BeanUtil.mapper(feedback,FeedbackDto.class);
                    dto.setIsRead(isReadMap.get(feedback.getId()));
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


    /**
     * 查询某个商户的所有通知
      * @param merchantId
     * @return
     */
    public List<NoticeDto> getNoticeListByMerchant(Long merchantId) {

        List<NoticeDto> result=Lists.emptyList();
        List<Notice> list=noticeDao.getNoticeListByMerchant(merchantId);

        if (CollectionUtils.isNotEmpty(list)) {
            result=list.stream().map(notice -> {
                NoticeDto dto=BeanUtil.mapper(notice,NoticeDto.class);
                return dto;
            }).collect(Collectors.toList());
        }

        return result;

    }
}
