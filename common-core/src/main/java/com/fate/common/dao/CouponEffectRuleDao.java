package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CouponEffectRule;
import com.fate.common.enums.EffectRule;

import java.util.List;

/**
 * <p>
 * 卡券生效规则表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-18
 */
public interface CouponEffectRuleDao extends IService<CouponEffectRule> {

    List<CouponEffectRule> getByCouponId(Long couponId);
}
