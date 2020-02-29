package com.fate.api.admin.service;

import com.fate.common.dao.StatisticDao;
import com.fate.common.dao.StatisticMonthSumDao;
import com.fate.common.dao.StatisticQuarterSumDao;
import com.fate.common.dao.StatisticWeekSumDao;
import com.fate.common.entity.Statistic;
import com.fate.common.entity.StatisticMonthSum;
import com.fate.common.entity.StatisticQuarterSum;
import com.fate.common.entity.StatisticWeekSum;
import com.fate.common.enums.StatisticType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 18:04
 **/
@Service
@Slf4j
public class StatisticService {
    @Resource
    StatisticDao statisticDao;
    @Resource
    StatisticWeekSumDao statisticWeekSumDao;
    @Resource
    StatisticMonthSumDao statisticMonthSumDao;
    @Resource
    StatisticQuarterSumDao statisticQuarterSumDao;

    public void insertBatch(List<Statistic> statisticList) {
        statisticDao.saveBatch(statisticList,100);
    }


    public void insert(Statistic statistic) {
        statisticDao.save(statistic);
    }

    public void deleteBy(Long merchantId, LocalDate date, StatisticType type) {
        statisticDao.deleteBy(merchantId,date,type);
    }

    public void deleteWeekSumBy(Long merchantId, Integer week, StatisticType type) {
        statisticWeekSumDao.deleteWeekSumBy(merchantId,week,type);
    }

    public void deleteMonthSumBy(Long merchantId, Integer month, StatisticType type) {
        statisticMonthSumDao.deleteMonthSumBy(merchantId,month,type);
    }

    public void deleteQuarterSumBy(Long merchantId, Integer quarter, StatisticType type) {
        statisticQuarterSumDao.deleteQuarterSumBy(merchantId,quarter,type);
    }

    public void insert(StatisticWeekSum statisticWeekSum) {
        Assert.isTrue(statisticWeekSumDao.save(statisticWeekSum),"插入数据失败");
    }

    public void insert(StatisticMonthSum statisticMonthSum) {
        statisticMonthSumDao.save(statisticMonthSum);
    }


    public void insert(StatisticQuarterSum statisticQuarterSum) {
        statisticQuarterSumDao.save(statisticQuarterSum);
    }

    public void insertWeekBatch(List<StatisticWeekSum> statisticWeekSumList) {
        statisticWeekSumDao.saveBatch(statisticWeekSumList,100);
    }

    public void insertMonthBatch(List<StatisticMonthSum> statisticMonthSumList) {
        statisticMonthSumDao.saveBatch(statisticMonthSumList,100);
    }

    public void insertQuarterBatch(List<StatisticQuarterSum> statisticQuarterSumList) {
        statisticQuarterSumDao.saveBatch(statisticQuarterSumList,100);
    }

    public List<Statistic> getStatisticBy(Long merchantId, StatisticType type, Integer quarter, Integer month, Integer week) {
        return statisticDao.getStatisticBy(merchantId,type,quarter,month,week);
    }
    public List<Statistic> getWeekStatisticBy(Long merchantId, StatisticType type, Integer week) {
        return statisticDao.getStatisticBy(merchantId,type,null,null,week);
    }
    public List<Statistic> getMonthStatisticBy(Long merchantId, StatisticType type, Integer month) {
        return getStatisticBy(merchantId,type,null,month,null);
    }
    public List<Statistic> getQuarterStatisticBy(Long merchantId, StatisticType type, Integer quarter) {
        return getStatisticBy(merchantId,type,quarter,null,null);
    }
}
