package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.StatisticMonthSum;
import com.fate.common.entity.StatisticWeekSum;
import com.fate.common.enums.StatisticType;

import java.util.List;

/**
 * <p>
 * 商户首页指标表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-08-26
 */
public interface StatisticMonthSumDao extends IService<StatisticMonthSum> {

    List<StatisticMonthSum> getList(Long shopId, Long userId, StatisticType statisticType, Integer year, Integer startMonth, Integer endMonth);

    void deleteMonthSumBy(Long merchantId, Integer month, StatisticType type);
}
