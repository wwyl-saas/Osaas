package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.MemberWelfare;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 会员卡固有属性信息表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-28
 */
public interface MemberWelfareMapper extends BaseMapper<MemberWelfare> {

    @SqlParser(filter = true)
    @Select("select * from t_member_welfare WHERE  merchant_id=#{merchantId} and member_id=#{memberId}")
    List<MemberWelfare> getByMemberIdAndMerchantId(Long merchantId ,Long memberId);
}
