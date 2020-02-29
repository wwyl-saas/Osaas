package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Merchant;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@SqlParser(filter = true)
public interface MerchantMapper extends BaseMapper<Merchant> {

    @SqlParser(filter = true)
    @Select("select id,name from  t_merchant t where enabled='1' ")
    List<Map<String,Object>> getMerchantIdAndName();
}
