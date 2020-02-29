package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MemberWelfareDao;
import com.fate.common.entity.MemberWelfare;
import com.fate.common.enums.MemberWelfareType;
import com.fate.common.mapper.MemberWelfareMapper;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * <p>
 * 会员卡固有属性信息表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-28
 */
@Repository
public class MemberWelfareDaoImpl extends ServiceImpl<MemberWelfareMapper, MemberWelfare> implements MemberWelfareDao {


    @Override
    public List<MemberWelfare> getByMemberId(Long memberId) {
        return  baseMapper.selectList(new QueryWrapper<MemberWelfare>().eq(MemberWelfare.MEMBER_ID,memberId));
    }

    @Override
    public List<MemberWelfare> getByMemberIdAndType(Long memberId, MemberWelfareType type) {
        return  baseMapper.selectList(new QueryWrapper<MemberWelfare>().eq(MemberWelfare.MEMBER_ID,memberId).eq(MemberWelfare.WELFARE_KEY,type));
    }

    @Override
    public List<MemberWelfare> getByMemberIdAndType(Long memberId, MemberWelfareType type, Long merchantId) {
        return  baseMapper.selectList(new QueryWrapper<MemberWelfare>().eq(MemberWelfare.MEMBER_ID,memberId).eq(MemberWelfare.WELFARE_KEY,type).eq(MemberWelfare.MERCHANT_ID,merchantId));
    }

    @Override
    public List<MemberWelfare> getByMemberIdAndMerchantId(Long merchantId, Long memberId) {
        return baseMapper.getByMemberIdAndMerchantId(merchantId,memberId);
    }
}
