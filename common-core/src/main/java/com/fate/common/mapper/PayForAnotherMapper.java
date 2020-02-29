package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fate.common.entity.PayForAnother;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-09
 */
public interface PayForAnotherMapper extends BaseMapper<PayForAnother> {

    int deleteByMap(@Param("cm") Map<String, Object> var1);

    @SqlParser(filter = true)
    @Override
    int delete(@Param("ew") Wrapper<PayForAnother> var1);


}
