package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.FeedbackUserDao;
import com.fate.common.entity.FeedbackUser;
import com.fate.common.mapper.FeedbackUserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商户用户反馈表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Repository
public class FeedbackUserDaoImpl extends ServiceImpl<FeedbackUserMapper, FeedbackUser> implements FeedbackUserDao {

    @Override
    public Integer getUnreadCount(Long userId) {
        return baseMapper.selectCount(new QueryWrapper<FeedbackUser>().eq(FeedbackUser.MERCHANT_USER_ID,userId).eq(FeedbackUser.IS_READ,false));
    }

    @Override
    public List<FeedbackUser> getListByUserId(Long userId) {
        return baseMapper.selectList(new QueryWrapper<FeedbackUser>().eq(FeedbackUser.MERCHANT_USER_ID,userId));
    }

    @Override
    public FeedbackUser getByUserIdAndFeedbackId(Long userId, Long feedbackId) {
        return baseMapper.selectOne(new QueryWrapper<FeedbackUser>().eq(FeedbackUser.MERCHANT_USER_ID,userId).eq(FeedbackUser.FEEKBACK_ID,feedbackId));
    }

}
