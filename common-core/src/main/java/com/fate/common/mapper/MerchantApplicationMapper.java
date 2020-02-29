package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fate.common.entity.MerchantApplication;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商户app表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@SqlParser(filter = true)
public interface MerchantApplicationMapper extends BaseMapper<MerchantApplication> {
    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    @Select("select * from t_merchant_application ${ew.customSqlSegment}")
    List<MerchantApplication> selectApplicationList(@Param(Constants.WRAPPER) Wrapper<MerchantApplication> queryWrapper);
}
