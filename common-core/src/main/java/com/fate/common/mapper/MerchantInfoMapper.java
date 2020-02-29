package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.MerchantInfo;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 商户详情表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantInfoMapper extends BaseMapper<MerchantInfo> {

    @SqlParser(filter = true)
    @Select("select * from t_merchant_info where merchant_id = #{merchantId} ")
    MerchantInfo getByMerchantId(Long merchantId);
}
