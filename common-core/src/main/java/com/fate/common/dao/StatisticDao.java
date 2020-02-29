package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Statistic;
import com.fate.common.enums.StatisticType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 商户首页指标表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-08-23
 */
public interface StatisticDao extends IService<Statistic> {

    List<Statistic> getStatisticDateList(Long shopId, Long userId, StatisticType dataType, LocalDate startDate, LocalDate endDate);

    BigDecimal getStatisticSum(Long userId, StatisticType dateRechargeFact);

    List<Statistic> getAverageListBy(Long shopId, Integer type, Integer year, Integer quarter, Integer month, Integer week);

    void deleteBy(Long merchantId, LocalDate date, StatisticType type);

    List<Statistic> getStatisticBy(Long merchantId, StatisticType type, Integer week);

    List<Statistic> getStatisticBy(Long merchantId, StatisticType type, Integer quarter, Integer month, Integer week);

    List<Statistic> getSumListBy(Long shopId, Integer type, Integer year, Integer quarter, Integer month, Integer week);
}
