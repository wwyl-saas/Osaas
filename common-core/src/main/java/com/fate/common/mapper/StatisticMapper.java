package com.fate.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Statistic;
import com.fate.common.enums.StatisticType;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商户首页指标表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-08-23
 */
public interface StatisticMapper extends BaseMapper<Statistic> {

    BigDecimal getStatisticSum(@Param("userId") Long userId,@Param("type") Integer type);

    List<Statistic> getAverageListBy(@Param("shopId")Long shopId,@Param("type") Integer type,@Param("year") Integer year,@Param("quarter") Integer quarter,@Param("month") Integer month, @Param("week")Integer week);

    List<Statistic> getSumListBy(@Param("shopId")Long shopId,@Param("type") Integer type,@Param("year") Integer year,@Param("quarter") Integer quarter,@Param("month") Integer month, @Param("week")Integer week);
}
