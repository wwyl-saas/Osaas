package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MemberWelfare;
import com.fate.common.enums.MemberWelfareType;

import java.util.List;

/**
 * <p>
 * 会员卡固有属性信息表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-28
 */
public interface MemberWelfareDao extends IService<MemberWelfare> {

    List<MemberWelfare> getByMemberId(Long memberId);

    List<MemberWelfare> getByMemberIdAndType(Long memberId, MemberWelfareType type);

    List<MemberWelfare> getByMemberIdAndType(Long memberId, MemberWelfareType type, Long merchantId);

    List<MemberWelfare> getByMemberIdAndMerchantId(Long merchantId, Long memberId);
}
