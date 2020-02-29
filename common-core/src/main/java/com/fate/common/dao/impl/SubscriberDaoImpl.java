package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.SubscriberDao;
import com.fate.common.entity.Subscriber;
import com.fate.common.mapper.SubscriberMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 企业公众号订阅者表（需商户用户订阅） 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-16
 */
@Repository
public class SubscriberDaoImpl extends ServiceImpl<SubscriberMapper, Subscriber> implements SubscriberDao {

    @Override
    public Subscriber getByOpenid(String mpOpenId) {
        return baseMapper.selectOne(new QueryWrapper<Subscriber>().eq(Subscriber.WX_OPENID,mpOpenId));
    }

    @Override
    public Subscriber getByUnionId(String wxUnionid) {
        return baseMapper.selectOne(new QueryWrapper<Subscriber>().eq(Subscriber.WX_UNOINID,wxUnionid));
    }
}
