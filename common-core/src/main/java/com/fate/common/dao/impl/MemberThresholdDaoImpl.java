package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MemberThresholdDao;
import com.fate.common.entity.MemberThreshold;

import com.fate.common.enums.MemberThresholdType;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.MemberThresholdMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 会员门槛值属性信息表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-30
 */
@Repository
public class MemberThresholdDaoImpl extends ServiceImpl<MemberThresholdMapper, MemberThreshold> implements MemberThresholdDao {

   @Override
    public List<MemberThreshold> getByMemberId(Long memberId){
       Assert.notNull(memberId, ResponseInfo.PARAM_NULL.getMsg());
        return  baseMapper.selectList(new QueryWrapper<MemberThreshold>().eq(MemberThreshold.MEMBER_ID,memberId));
    }

    @Override
    public List<MemberThreshold> getByThresholdType(MemberThresholdType thresholdType) {
        return  baseMapper.selectList(new QueryWrapper<MemberThreshold>().eq(MemberThreshold.THRESHOLD_KEY,thresholdType));
    }

    @Override
    public List<MemberThreshold> getThresholdByMemberId(Long memberId, Long merchantId) {
        return  baseMapper.selectList(new QueryWrapper<MemberThreshold>().eq(MemberThreshold.MEMBER_ID,memberId).eq(MemberThreshold.MERCHANT_ID,merchantId));
    }

    @Override
    public List<MemberThreshold> getByMemberIdAndMerchantId(Long merchantId, Long memberId) {
        return  baseMapper.getByMemberIdAndMerchantId(merchantId,memberId);
    }


}
