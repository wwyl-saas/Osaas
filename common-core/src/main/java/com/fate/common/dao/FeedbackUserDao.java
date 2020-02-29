package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.FeedbackUser;

import java.util.List;

/**
 * <p>
 * 商户用户反馈表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
public interface FeedbackUserDao extends IService<FeedbackUser> {

    Integer getUnreadCount(Long userId);

    List<FeedbackUser> getListByUserId(Long userId);

    FeedbackUser getByUserIdAndFeedbackId(Long id, Long feedbackId);
}
