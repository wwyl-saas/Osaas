package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.AnnounceDao;
import com.fate.common.entity.Announce;
import com.fate.common.mapper.AnnounceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Repository
public class AnnounceDaoImpl extends ServiceImpl<AnnounceMapper, Announce> implements AnnounceDao {

    @Override
    public Integer getCount() {
        return baseMapper.selectCount(new QueryWrapper<Announce>().eq(Announce.ENABLE,true));
    }

    @Override
    public List<Announce> getList() {
        return baseMapper.selectList(new QueryWrapper<Announce>().eq(Announce.ENABLE,true).orderByDesc(Announce.CREATE_TIME));
    }
}
