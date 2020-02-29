package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Member;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户会员福利表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MemberMapper extends BaseMapper<Member> {

    @SqlParser(filter = true)
    @Select("select * from  t_member  where merchant_id = #{merchantId} ")
    List<Member> getMemberCardByMercchantId(String merchantId);

    @SqlParser(filter = true)
    @Select("select * from  t_member")
    List<Member> getMemberCard();


    @SqlParser(filter = true)
    @Select("select id,level from  t_member where merchant_id = #{merchantId} ")
    List<Map<String, Object>> getMemberIdAndLevel(Long merchantId);
}
