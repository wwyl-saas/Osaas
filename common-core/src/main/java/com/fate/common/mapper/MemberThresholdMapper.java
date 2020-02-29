package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.MemberThreshold;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 会员门槛值属性信息表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-30
 */
public interface MemberThresholdMapper extends BaseMapper<MemberThreshold> {

    @SqlParser(filter = true)
    @Select("select * from t_member_threshold WHERE member_id=#{memberId} and merchant_id=#{merchantId}")
    List<MemberThreshold> getByMemberIdAndMerchantId(Long merchantId, Long memberId);


}
