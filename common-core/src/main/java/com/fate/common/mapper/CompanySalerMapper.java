package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.CompanySaler;

/**
 * <p>
 * 公司销售信息表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@SqlParser(filter = true)
public interface CompanySalerMapper extends BaseMapper<CompanySaler> {

}
