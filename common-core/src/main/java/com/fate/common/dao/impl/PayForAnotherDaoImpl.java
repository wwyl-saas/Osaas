package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate.common.entity.PayForAnother;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.PayForAnotherMapper;
import com.fate.common.dao.PayForAnotherDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-04
 */
@Repository
public class PayForAnotherDaoImpl extends ServiceImpl<PayForAnotherMapper, PayForAnother> implements PayForAnotherDao {

    @Override
    public PayForAnother getByPayCode(Long code) {
        Assert.notNull(code, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectOne(new QueryWrapper<PayForAnother>().eq(PayForAnother.PAY_CODE,code).isNull(PayForAnother.CONSUME_ID));
    }

    @Override
    public List<PayForAnother> getUnusedBeforeCreateTime(LocalDateTime datetime) {
        return baseMapper.selectList(new QueryWrapper<PayForAnother>().isNull(PayForAnother.CONSUME_ID).lt(PayForAnother.CREATE_TIME,datetime));
    }
}
