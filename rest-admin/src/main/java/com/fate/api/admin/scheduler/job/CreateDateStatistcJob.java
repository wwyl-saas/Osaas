package com.fate.api.admin.scheduler.job;

import com.fate.api.admin.service.*;
import com.fate.common.entity.*;
import com.fate.common.enums.StatisticType;
import com.fate.common.model.StatisticModel;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 创建每日统计日报(今日统计日报昨日的数据)（大数据量时需要分页查询）
 * @author: chenyixin
 * @create: 2019-09-25 15:32
 **/
@Component
@Slf4j
public class CreateDateStatistcJob extends Job {
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    CommentService commentService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    CustomerChargeService customerChargeService;
    @Autowired
    CustomerAppointmentService customerAppointmentService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerConsumeService customerConsumeService;
    @Autowired
    StatisticService statisticService;
    @Autowired
    MerchantShopService merchantShopService;

    @Override
    void work() {
        log.info("统计日报，开始执行......");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            List<Merchant> merchants = merchantService.getAllEnable();
            if (CollectionUtils.isNotEmpty(merchants)) {
                for (Merchant merchant : merchants) {
                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    merchantStatisticWork(merchant.getId(), yesterday);
                }
            }
            stopWatch.stop();
            log.info("统计日报执行完毕，用时{}ms", stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            log.error("统计日报报错：", e);
        }
    }

    /**
     * 统计日报商户数据
     */
    private void merchantStatisticWork(Long merchantId, LocalDate date) {
        try {
            statisticCustomerVisit(merchantId, date);
            statisticAuthorization(merchantId, date);
            statisticMemberIncrease(merchantId, date);
            List<MerchantShop> merchantShops = merchantShopService.getAllEnable(merchantId);
            Assert.notEmpty(merchantShops, "商户门店设置为空");
            List<MerchantUserRole> merchantUserRoles = merchantUserService.getAllUserRoles(merchantId);
            Assert.notEmpty(merchantUserRoles, "商户用户的角色设置为空");
            if (CollectionUtils.isNotEmpty(merchantUserRoles) && CollectionUtils.isNotEmpty(merchantShops)) {
                statisticEvaluate(merchantShops, merchantUserRoles, merchantId, date);
                statisticMemberCharge(merchantShops, merchantUserRoles, merchantId, date);
                statisticMemberAppoinment(merchantShops, merchantUserRoles, merchantId, date);
                statisticMemberOrder(merchantShops, merchantUserRoles, merchantId, date);
                statisticFactCharge(merchantShops, merchantUserRoles, merchantId, date);
                statisticGiftCharge(merchantShops, merchantUserRoles, merchantId, date);
                statisticAccountCharge(merchantShops, merchantUserRoles, merchantId, date);
                statisticFactConsume(merchantShops, merchantUserRoles, merchantId, date);
                statisticRemainderConsume(merchantShops, merchantUserRoles, merchantId, date);
                statisticConsume(merchantShops, merchantUserRoles, merchantId, date);
            } else {

            }
        } catch (Exception e) {
            log.error("商户{}统计日报报错", merchantId);
        }
    }

    /**
     * 更新统计日报表商铺用户评价分值和用户表评价分值
     * EVALUATE(0,"会员评价","分"),//商户、门店、个人（精确划分）
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticEvaluate(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            //门店 员工 平均得分
            StatisticType type = StatisticType.EVALUATE;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = commentService.getShopAverageEvaluate(merchantId);


            //商户评价
            BigDecimal dataValue = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(statisticModels)) {
                IntSummaryStatistics merchantSummary = statisticModels.parallelStream().mapToInt(statisticModel -> statisticModel.getDataValue().intValue()).summaryStatistics();
                dataValue = BigDecimal.valueOf(merchantSummary.getAverage());
            }
            Statistic merchantStatistic = createStatistic(date, merchantId, null, null, type, dataValue);
            statisticService.insert(merchantStatistic);

            //门店平均
            int shopsSize = merchantShops.size();
            List<Statistic> shopStatisticList = new ArrayList<>(shopsSize);
            merchantShops.forEach(merchantShop -> {
                BigDecimal dataValue1 = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(statisticModels)) {
                    IntSummaryStatistics summaryStatistics = statisticModels.parallelStream().filter(statisticModel -> statisticModel.getShopId() != null && statisticModel.getShopId() > 0).filter(statisticModel -> statisticModel.getShopId().equals(merchantShop.getId()))
                            .mapToInt(statisticModel -> statisticModel.getDataValue().intValue()).summaryStatistics();
                    dataValue1 = BigDecimal.valueOf(summaryStatistics.getAverage());
                }
                Statistic statistic = createStatistic(date, merchantId, merchantShop.getId(), null, type, dataValue1);
                shopStatisticList.add(statistic);
            });
            statisticService.insertBatch(shopStatisticList);

            //用户个人平均
            userStatistic(merchantUserRoles, date, merchantId, StatisticType.EVALUATE, statisticModels);

            //计算每个人的评价分值
            if (CollectionUtils.isNotEmpty(statisticModels)) {
                List<StatisticModel> users = commentService.getAverageEvaluate(merchantId);
                Map<Long, BigDecimal> userMap = new HashMap<>();
                users.parallelStream().filter(statisticModel -> statisticModel.getMerchantUserId() > 0).forEach(evaluateModel -> userMap.put(evaluateModel.getMerchantUserId(), evaluateModel.getDataValue()));
                List<MerchantUser> merchantUsers = merchantUserService.getAllUsers(merchantId);
                if (CollectionUtils.isNotEmpty(merchantUsers)) {
                    merchantUsers.parallelStream().forEach(user -> {
                        BigDecimal grade = userMap.get(user.getId());
                        if (grade != null) {
                            user.setGrade(grade.intValue());
                        }
                    });
                    merchantUserService.updateBatch(merchantUsers);
                }
            }
        } catch (Exception e) {
            log.error("统计日报用户表评价分值出错：", e);
        }
    }

    /**
     * 统计日报线上访问
     * DATE_VISIT(1, "线上访问","人"),//商户
     *
     * @param merchantId
     */
    private void statisticCustomerVisit(Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_VISIT;
            statisticService.deleteBy(merchantId, date, type);
            Integer visitCount = customerService.getVisitCount(merchantId, date);
            Statistic statistic = createStatistic(date, merchantId, null, null, type, BigDecimal.valueOf(visitCount.longValue()));
            statisticService.insert(statistic);
        } catch (Exception e) {
            log.error("统计日报线上访问数据日报出错：", e);
        }
    }

    /**
     * 客户增量
     * DATE_CUSTOMER(2, "客户增量","人"),//商户
     *
     * @param merchantId
     */
    private void statisticAuthorization(Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_CUSTOMER;
            statisticService.deleteBy(merchantId, date, type);
            Integer customerCount = customerService.getNewCustomerCount(merchantId, date);
            Statistic statistic = createStatistic(date, merchantId, null, null, type, BigDecimal.valueOf(customerCount));
            statisticService.insert(statistic);
        } catch (Exception e) {
            log.error("统计日报客户增量数据日报出错：", e);
        }
    }

    /**
     * 会员增量
     * DATE_MEMBER_INCREASE(3, "会员增量","人"),//商户
     */
    private void statisticMemberIncrease(Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_MEMBER_INCREASE;
            statisticService.deleteBy(merchantId, date, type);
            Integer memberCount = customerAccountService.getNewMemberCount(merchantId, date);
            Statistic statistic = createStatistic(date, merchantId, null, null, type, BigDecimal.valueOf(memberCount));
            statisticService.insert(statistic);
        } catch (Exception e) {
            log.error("统计日报会员增量数据日报出错：", e);
        }
    }

    /**
     * 充值会员
     * DATA_MEMBER_RECHARGE(4, "充值会员","人"),//商户、门店、个人（部分到商户）
     *
     * @param shops
     * @param userRoles
     * @param merchantId
     */
    private void statisticMemberCharge(List<MerchantShop> shops, List<MerchantUserRole> userRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATA_MEMBER_RECHARGE;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerChargeService.getChargeCount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(shops, date, merchantId, type, statisticModels);
            userStatistic(userRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报充值会员数据日报出错：", e);
        }
    }


    /**
     * 预约服务
     * DATE_APPOINTMENT(5, "预约服务","个"),//商户、门店、个人（部分到门店）
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticMemberAppoinment(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_APPOINTMENT;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerAppointmentService.getAppointmentCount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报预约服务数据日报出错：", e);
        }
    }

    /**
     * 交易订单
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticMemberOrder(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_ORDER;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = orderService.getOrderCount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);

        } catch (Exception e) {
            log.error("统计日报交易订单数据日报出错：", e);
        }
    }


    /**
     * 实际充值
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticFactCharge(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_RECHARGE_FACT;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerChargeService.getFactChargeAmount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报充值金额数据日报出错：", e);
        }
    }


    /**
     * 充值返赠
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticGiftCharge(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_RECHARGE_GIFT;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerChargeService.getGiftChargeAmount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报充值金额数据日报出错：", e);
        }
    }

    /**
     * 账面充值
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticAccountCharge(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_ACCOUNT_RECHARGE;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerChargeService.getChargeAmount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报充值金额数据日报出错：", e);
        }
    }


    /**
     * 实际收款
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticFactConsume(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_CONSUME_FACT;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerConsumeService.getFactConsumeAmount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报会员消费金额数据日报出错：", e);
        }
    }

    /**
     * 储值消耗
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticRemainderConsume(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_CONSUME_STORE;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerConsumeService.getRemainderConsumeAmount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报会员消费金额数据日报出错：", e);
        }
    }

    /**
     * 账面营业
     *
     * @param merchantShops
     * @param merchantUserRoles
     * @param merchantId
     */
    private void statisticConsume(List<MerchantShop> merchantShops, List<MerchantUserRole> merchantUserRoles, Long merchantId, LocalDate date) {
        try {
            StatisticType type = StatisticType.DATE_CONSUME;
            statisticService.deleteBy(merchantId, date, type);
            List<StatisticModel> statisticModels = customerConsumeService.getConsumeAmount(merchantId, date);

            merchantStatistic(date, merchantId, type, statisticModels);
            shopStatistic(merchantShops, date, merchantId, type, statisticModels);
            userStatistic(merchantUserRoles, date, merchantId, type, statisticModels);
        } catch (Exception e) {
            log.error("统计日报会员消费金额数据日报出错：", e);
        }
    }

    /**
     * 创建统计日报对象
     *
     * @param yesterday
     * @param merchantId
     * @param shopId
     * @param userId
     * @param type
     * @param dataValue
     * @return
     */
    private Statistic createStatistic(LocalDate yesterday, Long merchantId, Long shopId, Long userId, StatisticType type, BigDecimal dataValue) {
        Statistic statistic = new Statistic()
                .setDataDate(yesterday)
                .setMerchantId(merchantId)
                .setMerchantUserId(userId != null ? userId : 0)
                .setShopId(shopId != null ? shopId : 0)
                .setType(type)
                .setYear(yesterday.getYear())
                .setMonth(yesterday.getMonthValue())
                .setWeek(DateUtil.getWeekOfWeekBasedYear(yesterday))
                .setQuarter(DateUtil.getQuarterByLocalDate(yesterday))
                .setDataValue(dataValue.setScale(0, BigDecimal.ROUND_HALF_UP));
        return statistic;
    }

    /**
     * 获取商户统计日报数据
     *
     * @param yesterday
     * @param merchantId
     * @param type
     * @param statisticModels
     * @return
     */
    private void merchantStatistic(LocalDate yesterday, Long merchantId, StatisticType type, List<StatisticModel> statisticModels) {
        BigDecimal dataValue = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(statisticModels)) {
            dataValue = statisticModels.parallelStream().map(statisticModel -> statisticModel.getDataValue()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }
        Statistic statistic = createStatistic(yesterday, merchantId, null, null, type, dataValue);
        statisticService.insert(statistic);
    }

    /**
     * 门店统计日报
     *
     * @param shops
     * @param yesterday
     * @param merchantId
     * @param type
     * @param statisticModels
     */
    private void shopStatistic(List<MerchantShop> shops, LocalDate yesterday, Long merchantId, StatisticType type, List<StatisticModel> statisticModels) {
        int size = shops.size();
        List<Statistic> statisticList = new ArrayList<>(size);
        shops.forEach(merchantShop -> {
            BigDecimal dataValue = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(statisticModels)) {
                dataValue = statisticModels.parallelStream().filter(statisticModel -> statisticModel.getShopId() != null && statisticModel.getShopId() > 0).filter(statisticModel -> statisticModel.getShopId().equals(merchantShop.getId()))
                        .map(statisticModel -> statisticModel.getDataValue()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            }
            Statistic shopStatistic = createStatistic(yesterday, merchantId, merchantShop.getId(), null, type, dataValue);
            statisticList.add(shopStatistic);
        });
        statisticService.insertBatch(statisticList);
    }

    /**
     * 用户统计日报
     *
     * @param userRoles
     * @param yesterday
     * @param merchantId
     * @param type
     * @param statisticModels
     */
    private void userStatistic(List<MerchantUserRole> userRoles, LocalDate yesterday, Long merchantId, StatisticType type, List<StatisticModel> statisticModels) {
        int size = statisticModels.size() * 2;
        Map<String, BigDecimal> statisticModelMap = new HashMap<>(size);
        if (CollectionUtils.isNotEmpty(statisticModels)) {
            statisticModels.parallelStream().filter(statisticModel -> statisticModel.getMerchantUserId() != null && statisticModel.getMerchantUserId() > 0).forEach(statisticModel -> {
                String key = new StringBuilder().append(statisticModel.getShopId()).append("|").append(statisticModel.getMerchantUserId()).toString();
                statisticModelMap.put(key, statisticModel.getDataValue());
            });
        }

        List<Statistic> statisticList = new ArrayList<>();
        userRoles.stream().forEach(merchantUserRole -> {
            String key = new StringBuilder().append(merchantUserRole.getShopId()).append("|").append(merchantUserRole.getUserId()).toString();
            BigDecimal dataValue = statisticModelMap.get(key);
            if (dataValue == null) {
                dataValue = BigDecimal.ZERO;
            }
            Statistic statistic = createStatistic(yesterday, merchantId, merchantUserRole.getShopId(), merchantUserRole.getUserId(), type, dataValue);
            statisticList.add(statistic);
        });
        statisticService.insertBatch(statisticList);
    }

}
