package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.AnnounceUserDao;
import com.fate.common.entity.AnnounceUser;
import com.fate.common.mapper.AnnounceUserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 公告查看表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Repository
public class AnnounceUserDaoImpl extends ServiceImpl<AnnounceUserMapper, AnnounceUser> implements AnnounceUserDao {

    @Override
    public Integer getCountBy(Long userId) {
        return baseMapper.selectCount(new QueryWrapper<AnnounceUser>().eq(AnnounceUser.MERCHANT_USER_ID,userId));
    }

    @Override
    public List<AnnounceUser> getListByUserIdAndAnnounceIds(Long userId, List<Long> announceIds) {
        return baseMapper.selectList(new QueryWrapper<AnnounceUser>().eq(AnnounceUser.MERCHANT_USER_ID,userId).in(AnnounceUser.ANNOUNCE_ID,announceIds));
    }

    @Override
    public AnnounceUser getByUserIdAndAnnounceId(Long userId, Long announceId) {
        return baseMapper.selectOne(new QueryWrapper<AnnounceUser>().eq(AnnounceUser.MERCHANT_USER_ID,userId).eq(AnnounceUser.ANNOUNCE_ID,announceId));
    }
}
