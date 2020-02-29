package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CouponEffectRuleDao;
import com.fate.common.entity.CouponEffectRule;
import com.fate.common.enums.EffectRule;
import com.fate.common.mapper.CouponEffectRuleMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 卡券生效规则表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-18
 */
@Repository
public class CouponEffectRuleDaoImpl extends ServiceImpl<CouponEffectRuleMapper, CouponEffectRule> implements CouponEffectRuleDao {

    @Override
    public List<CouponEffectRule> getByCouponId(Long couponId) {
        return baseMapper.selectList(new QueryWrapper<CouponEffectRule>().eq(CouponEffectRule.COUPON_ID,couponId));
    }
}
