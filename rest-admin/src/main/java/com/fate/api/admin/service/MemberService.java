package com.fate.api.admin.service;

import com.fate.common.dao.MemberDao;
import com.fate.common.dao.MemberThresholdDao;
import com.fate.common.dao.MemberWelfareDao;
import com.fate.common.entity.Member;
import com.fate.common.entity.MemberThreshold;
import com.fate.common.entity.MemberWelfare;
import com.fate.common.enums.MemberWelfareType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 17:02
 **/
@Service
@Slf4j
public class MemberService {
    @Resource
    MemberDao memberDao;
    @Resource
    MemberThresholdDao memberThresholdDao;
    @Resource
    MemberWelfareDao memberWelfareDao;


    public List<MemberWelfare> getByMemberIdAndType(Long memberId, MemberWelfareType type, Long merchantId) {
        return memberWelfareDao.getByMemberIdAndType(memberId,type,merchantId);
    }

    /**
     *  按商户会员ID查询门槛值
     * @param memberId
     * @param merchantId
     * @return
     */
    public List<MemberThreshold> getThresholdByMemberId(Long memberId, Long merchantId) {
        return memberThresholdDao.getThresholdByMemberId(memberId,merchantId);
    }

    /**
     * 按等级查询商户会员信息
     * @param level
     * @param merchantId
     * @return
     */
    public Member getByLevel(Integer level, Long merchantId) {
        return memberDao.getByLevel(level,merchantId);
    }
}
