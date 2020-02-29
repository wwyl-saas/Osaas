package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MemberDao;
import com.fate.common.entity.Member;
import com.fate.common.entity.MemberThreshold;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.MemberMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户会员福利表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MemberDaoImpl extends ServiceImpl<MemberMapper, Member> implements MemberDao {

    @Override
    public Member findByMemberLevel(Integer memberLevel) {
        Assert.notNull(memberLevel, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectOne(new QueryWrapper<Member>().eq(Member.LEVEL,memberLevel));
    }

    @Override
    public Member getByLevel(Integer level, Long merchantId) {
        return baseMapper.selectOne(new QueryWrapper<Member>().eq(Member.LEVEL,level).eq(Member.MERCHANT_ID,merchantId));
    }

    @Override
    public List<Member> getMemberCardByMercchantId(String merchantId) {
       return  baseMapper.getMemberCardByMercchantId(merchantId);

    }

    @Override
    public List<Member> getMemberCard() {
        return  baseMapper.getMemberCard();

    }

    @Override
    public List<Map<String, Object>> getMemberIdAndLevel(Long merchantId) {
        return  baseMapper.getMemberIdAndLevel(merchantId);
    }


}
