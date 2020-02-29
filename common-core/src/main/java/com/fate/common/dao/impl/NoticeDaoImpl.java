package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.NoticeDao;
import com.fate.common.entity.Notice;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.NoticeMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 消息通知表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Repository
public class NoticeDaoImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeDao {

    @Override
    public Integer getUnreadNoticeCount(Long userId) {
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectCount(new QueryWrapper<Notice>().eq(Notice.MERCHANT_USER_ID,userId).eq(Notice.ISREAD,false));
    }

    @Override
    public List<Notice> getListByUserId(Long userId) {
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Notice>().eq(Notice.MERCHANT_USER_ID,userId).orderByDesc(Notice.CREATE_TIME));
    }

    @Override
    public List<Notice> getNoticeListByMerchant(Long merchantId) {
        return baseMapper.getNoticeListByMerchant(merchantId);
    }
}
