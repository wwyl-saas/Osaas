package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Member;
import com.fate.common.entity.MemberThreshold;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户会员福利表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MemberDao extends IService<Member> {

    Member findByMemberLevel(Integer memberLevel);

    Member getByLevel(Integer level, Long merchantId);

    List<Member> getMemberCardByMercchantId(String merchantId);

    List<Member> getMemberCard();

    List<Map<String, Object>> getMemberIdAndLevel(Long merchantId);
}
