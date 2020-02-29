package com.fate.api.admin.scheduler.job;

import com.fate.api.admin.service.MerchantService;
import com.fate.api.admin.service.StatisticService;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.Statistic;
import com.fate.common.entity.StatisticWeekSum;
import com.fate.common.enums.StatisticType;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 统计周数据
 * @author: chenyixin
 * @create: 2019-09-25 15:40
 **/
@Component
@Slf4j
public class CreateWeekStatisticJob extends Job{
    @Autowired
    StatisticService statisticService;
    @Autowired
    MerchantService merchantService;

    @Override
    void work() {
        log.info("统计周报，开始执行......");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            List<Merchant> merchants = merchantService.getAllEnable();
            if (CollectionUtils.isNotEmpty(merchants)) {
                for (Merchant merchant : merchants) {
                    LocalDate lastWeekday = LocalDate.now().minusWeeks(1);
                    Integer year=lastWeekday.getYear();
                    Integer week= DateUtil.getWeekOfWeekBasedYear(lastWeekday);
                    merchantStatisticWork(merchant.getId(),year, week);
                }
            }
            stopWatch.stop();
            log.info("统计周报执行完毕，用时{}ms", stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            log.error("统计周报报错：", e);
        }


    }

    /**
     * 开始统计周报
     * @param merchantId
     * @param week
     */
    private void merchantStatisticWork(Long merchantId,Integer year,Integer week) {
        statisticEvaluate(merchantId,year, week);
        statisticCustomerVisit(merchantId,year, week);
        statisticAuthorization(merchantId,year, week);
        statisticMemberIncrease(merchantId,year, week);
        statisticMemberCharge(merchantId,year, week);
        statisticMemberAppoinment(merchantId,year, week);
        statisticMemberOrder(merchantId,year, week);
        statisticFactCharge(merchantId,year, week);
        statisticGiftCharge(merchantId,year, week);
        statisticAccountCharge(merchantId,year, week);
        statisticFactConsume(merchantId,year, week);
        statisticRemainderConsume(merchantId,year, week);
        statisticConsume(merchantId,year, week);
    }


    private void statisticEvaluate(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type=StatisticType.EVALUATE;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            //商户
            if (CollectionUtils.isNotEmpty(statisticList)){
                IntSummaryStatistics merchantSummary = statisticList.parallelStream().filter(statistic -> statistic.getShopId().equals(0L) && statistic.getMerchantUserId().equals(0L)).mapToInt(statisticModel -> statisticModel.getDataValue().intValue()).summaryStatistics();
                BigDecimal dataValue = BigDecimal.valueOf(merchantSummary.getAverage());
                StatisticWeekSum statisticWeekSum=createStatisticWeekSum(year,week,merchantId,null,null,type,dataValue);
                statisticService.insert(statisticWeekSum);
            }
            //门店
            if (CollectionUtils.isNotEmpty(statisticList)){
                Map<Long,List<Statistic>> statisticMap=statisticList.parallelStream().filter(statistic -> statistic.getShopId()>0 && statistic.getMerchantUserId().equals(0L)).collect(Collectors.groupingBy(Statistic::getShopId));
                List<StatisticWeekSum> statisticWeekSumList=new ArrayList<>();
                for (Map.Entry<Long,List<Statistic>> entry:statisticMap.entrySet()){
                    IntSummaryStatistics merchantSummary =entry.getValue().parallelStream().mapToInt(statistic -> statistic.getDataValue().intValue()).summaryStatistics();
                    BigDecimal dataValue = BigDecimal.valueOf(merchantSummary.getAverage());
                    StatisticWeekSum statisticWeekSum=createStatisticWeekSum(year,week,merchantId,entry.getKey(),null,type,dataValue);
                    statisticWeekSumList.add(statisticWeekSum);
                }
                statisticService.insertWeekBatch(statisticWeekSumList);
            }
            //个人
            if (CollectionUtils.isNotEmpty(statisticList)){
                List<StatisticWeekSum> statisticWeekSumList=new ArrayList<>();
                Map<String,List<Statistic>> listMap=statisticList.parallelStream().filter(statistic -> statistic.getMerchantUserId()>0)
                        .collect(Collectors.groupingBy(statistic -> new StringBuilder().append(statistic.getShopId()).append("-").append(statistic.getMerchantUserId()).toString()));
                for (Map.Entry<String,List<Statistic>> entry:listMap.entrySet()){
                    String[] array=entry.getKey().split("-");
                    IntSummaryStatistics merchantSummary =entry.getValue().parallelStream().mapToInt(statistic -> statistic.getDataValue().intValue()).summaryStatistics();
                    BigDecimal dataValue = BigDecimal.valueOf(merchantSummary.getAverage());
                    StatisticWeekSum statisticWeekSum=createStatisticWeekSum(year,week,merchantId,Long.parseLong(array[0]),Long.parseLong(array[1]),type,dataValue);
                    statisticWeekSumList.add(statisticWeekSum);
                }
                statisticService.insertWeekBatch(statisticWeekSumList);
            }

        } catch (Exception e) {
            log.error("统计周报用户表评价分值出错：", e);
        }

    }

    /**
     * 统计周报线上访问
     * DATE_VISIT(1, "线上访问","人"),//商户
     *
     * @param merchantId
     */
    private void statisticCustomerVisit(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_VISIT;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);
            merchantStatistic(year,week,merchantId,type,statisticList);

        } catch (Exception e) {
            log.error("统计周报线上访问数据日报出错：", e);
        }
    }

    /**
     * 客户增量
     * DATE_CUSTOMER(2, "客户增量","人"),//商户
     *
     * @param merchantId
     */
    private void statisticAuthorization(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_CUSTOMER;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);
            merchantStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报客户增量数据日报出错：", e);
        }
    }

    /**
     * 会员增量
     * DATE_MEMBER_INCREASE(3, "会员增量","人"),//商户
     */
    private void statisticMemberIncrease(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_MEMBER_INCREASE;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);
            merchantStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报会员增量数据日报出错：", e);
        }
    }

    /**
     * 充值会员
     * DATA_MEMBER_RECHARGE(4, "充值会员","人"),//商户、门店、个人（部分到商户）
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticMemberCharge(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATA_MEMBER_RECHARGE;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报充值会员数据日报出错：", e);
        }
    }


    /**
     *   预约服务
     *    DATE_APPOINTMENT(5, "预约服务","个"),//商户、门店、个人（部分到门店）
     * @param merchantId
     * @param week
     */
    private void statisticMemberAppoinment(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_APPOINTMENT;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);


            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报预约服务数据日报出错：", e);
        }
    }

    /**
     * 交易订单
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticMemberOrder(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_ORDER;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报交易订单数据日报出错：", e);
        }
    }


    /**
     * 实际充值
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticFactCharge(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_RECHARGE_FACT;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报充值金额数据日报出错：", e);
        }
    }


    /**
     * 充值返赠
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticGiftCharge(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_RECHARGE_GIFT;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报充值金额数据日报出错：", e);
        }
    }

    /**
     * 账面充值
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticAccountCharge(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_ACCOUNT_RECHARGE;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报充值金额数据日报出错：", e);
        }
    }


    /**
     * 实际收款
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticFactConsume(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_CONSUME_FACT;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报会员消费金额数据日报出错：", e);
        }
    }

    /**
     * 储值消耗
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticRemainderConsume(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_CONSUME_STORE;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报会员消费金额数据日报出错：", e);
        }
    }

    /**
     * 账面营业
     *
     * @param merchantId
     * @param week
     * @param merchantId
     */
    private void statisticConsume(Long merchantId,Integer year, Integer week){
        try {
            StatisticType type = StatisticType.DATE_CONSUME;
            statisticService.deleteWeekSumBy(merchantId,week,type);
            List<Statistic> statisticList=statisticService.getWeekStatisticBy(merchantId,type,week);

            merchantStatistic(year,week,merchantId,type,statisticList);
            shopStatistic(year,week,merchantId,type,statisticList);
            userStatistic(year,week,merchantId,type,statisticList);
        } catch (Exception e) {
            log.error("统计周报会员消费金额数据日报出错：", e);
        }
    }

    /**
     * 创建统计周报对象
     *
     * @param merchantId
     * @param shopId
     * @param userId
     * @param type
     * @param dataValue
     * @return
     */
    private StatisticWeekSum createStatisticWeekSum(Integer year,Integer week, Long merchantId, Long shopId, Long userId, StatisticType type, BigDecimal dataValue) {
        StatisticWeekSum statistic = new StatisticWeekSum()
                .setMerchantId(merchantId)
                .setMerchantUserId(userId != null ? userId : 0)
                .setShopId(shopId != null ? shopId : 0)
                .setType(type)
                .setYear(year)
                .setWeek(week)
                .setDataValue(dataValue.setScale(0, BigDecimal.ROUND_HALF_UP));
        return statistic;
    }


    /**
     * 统计周报用户的
     * @param week
     * @param merchantId
     * @param type
     * @param statisticList
     */
    private void userStatistic(Integer year,Integer week, Long merchantId, StatisticType type, List<Statistic> statisticList) {
        if (CollectionUtils.isNotEmpty(statisticList)){
            List<StatisticWeekSum> statisticWeekSumList=new ArrayList<>();
            Map<String,List<Statistic>> listMap=statisticList.parallelStream().filter(statistic -> statistic.getMerchantUserId()>0)
                    .collect(Collectors.groupingBy(statistic -> new StringBuilder().append(statistic.getShopId()).append("-").append(statistic.getMerchantUserId()).toString()));
            for (Map.Entry<String,List<Statistic>> entry:listMap.entrySet()){
                String[] array=entry.getKey().split("-");
                BigDecimal dataValue=entry.getValue().parallelStream().map(statistic -> statistic.getDataValue()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                StatisticWeekSum statisticWeekSum=createStatisticWeekSum(year,week,merchantId,Long.parseLong(array[0]),Long.parseLong(array[1]),type,dataValue);
                statisticWeekSumList.add(statisticWeekSum);
            }
            statisticService.insertWeekBatch(statisticWeekSumList);
        }
    }

    /**
     * 统计周报门店的
     * @param week
     * @param merchantId
     * @param type
     * @param statisticList
     */
    private void shopStatistic(Integer year,Integer week, Long merchantId, StatisticType type, List<Statistic> statisticList) {
        if (CollectionUtils.isNotEmpty(statisticList)){
            Map<Long,List<Statistic>> statisticMap=statisticList.parallelStream().filter(statistic -> statistic.getShopId()>0 && statistic.getMerchantUserId().equals(0L))
                    .collect(Collectors.groupingBy(Statistic::getShopId));
            List<StatisticWeekSum> statisticWeekSumList=new ArrayList<>();
            for (Map.Entry<Long,List<Statistic>> entry:statisticMap.entrySet()){
                BigDecimal dataValue=entry.getValue().parallelStream().map(statistic -> statistic.getDataValue()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                StatisticWeekSum statisticWeekSum=createStatisticWeekSum(year,week,merchantId,entry.getKey(),null,type,dataValue);
                statisticWeekSumList.add(statisticWeekSum);
            }
            statisticService.insertWeekBatch(statisticWeekSumList);
        }
    }

    /**
     * 统计周报商户的
     * @param week
     * @param merchantId
     * @param type
     * @param statisticList
     */
    private void merchantStatistic(Integer year,Integer week, Long merchantId, StatisticType type, List<Statistic> statisticList) {
        if (CollectionUtils.isNotEmpty(statisticList)){
            BigDecimal dataValue=statisticList.parallelStream().filter(statistic -> statistic.getShopId().equals(0L)&& statistic.getMerchantUserId().equals(0L))
                    .map(statistic -> statistic.getDataValue()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            StatisticWeekSum statisticWeekSum=createStatisticWeekSum(year,week,merchantId,null,null,type,dataValue);
            statisticService.insert(statisticWeekSum);
        }
    }
}
