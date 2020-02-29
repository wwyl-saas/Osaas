package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MemberThreshold;
import com.fate.common.enums.MemberThresholdType;

import java.util.List;

/**
 * <p>
 * 会员门槛值属性信息表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-30
 */
public interface MemberThresholdDao extends IService<MemberThreshold> {

    List<MemberThreshold> getByMemberId(Long memberId);

    List<MemberThreshold> getByThresholdType(MemberThresholdType chargeAmount);

    List<MemberThreshold> getThresholdByMemberId(Long memberId, Long merchantId);

    List<MemberThreshold> getByMemberIdAndMerchantId(Long merchantId, Long memberId);
}
