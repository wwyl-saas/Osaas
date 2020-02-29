package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.enums.ConsumeType;
import com.fate.common.enums.StatisticType;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 统计数据相关
 * @author: chenyixin
 * @create: 2019-07-19 14:02
 **/
@Service
@Slf4j
public class StatisticsService {
    private static final String CUSTOMER_DATA = "/pages/data/member";
    private static final String APPOINTMENT_DATA = "/pages/data/appointment";
    private static final String ORDER_DATA = "/pages/data/order";
    private static final String RECHARGE_DATA = "/pages/data/charge";

    @Resource
    StatisticDao statisticDao;
    @Resource
    StatisticWeekSumDao statisticWeekSumDao;
    @Resource
    StatisticMonthSumDao statisticMonthSumDao;
    @Resource
    StatisticQuarterSumDao statisticQuarterSumDao;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    ChargeService chargeService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerConsumeService customerConsumeService;
    @Autowired
    MerchantUserService merchantUserService;

    /**
     * 获取门店今日数据
     *
     * @param shopId
     * @return
     */
    @Cacheable(value = "shopStatistics", unless = "#result == null", key = "#shopId")
    public List<DataAttributeDto> getShopStatistic(Long shopId) {
        List<DataAttributeDto> result = new ArrayList<>();
        //获取今日访问（和门店无关）
        Integer visitCount = customerService.getTodayVisitCount();
        DataAttributeDto visitCountDto = DataAttributeDto.builder().name(StatisticType.DATE_VISIT.getDesc()).value(visitCount.toString())
                .unit(StatisticType.DATE_VISIT.getUnit()).url(CUSTOMER_DATA).id(StatisticType.DATE_VISIT.getCode().longValue()).build();
        result.add(visitCountDto);
        //客户增量
        Integer customerIncreaseCount = customerService.getTodayNewCustomerCount();
        DataAttributeDto customerIncreaseDto = DataAttributeDto.builder().name(StatisticType.DATE_CUSTOMER.getDesc()).value(customerIncreaseCount.toString())
                .unit(StatisticType.DATE_CUSTOMER.getUnit()).url(CUSTOMER_DATA).id(StatisticType.DATE_CUSTOMER.getCode().longValue()).build();
        result.add(customerIncreaseDto);
        //会员增量
        Integer memberIncreaseCount = customerAccountService.getTodayNewMemberCount();
        DataAttributeDto memberIncreaseDto = DataAttributeDto.builder().name(StatisticType.DATE_MEMBER_INCREASE.getDesc()).value(memberIncreaseCount.toString())
                .unit(StatisticType.DATE_MEMBER_INCREASE.getUnit()).url(CUSTOMER_DATA).id(StatisticType.DATE_MEMBER_INCREASE.getCode().longValue()).build();
        result.add(memberIncreaseDto);
        //会员新充值
        Integer memberRechargeCount = chargeService.getTodayNewChargeCount(shopId);
        DataAttributeDto memberRechargeDto = DataAttributeDto.builder().name(StatisticType.DATA_MEMBER_RECHARGE.getDesc()).value(memberRechargeCount.toString())
                .unit(StatisticType.DATA_MEMBER_RECHARGE.getUnit()).url(CUSTOMER_DATA).id(StatisticType.DATA_MEMBER_RECHARGE.getCode().longValue()).build();
        result.add(memberRechargeDto);
        //预约量
        Integer appointmentCount = appointmentService.getTodayAppointmentCount(shopId);
        DataAttributeDto appointmentDto = DataAttributeDto.builder().name(StatisticType.DATE_APPOINTMENT.getDesc()).value(appointmentCount.toString())
                .unit(StatisticType.DATE_APPOINTMENT.getUnit()).url(APPOINTMENT_DATA).id(StatisticType.DATE_APPOINTMENT.getCode().longValue()).build();
        result.add(appointmentDto);
        //订单量
        Integer orderCount = orderService.getTodayOrderCount(shopId);
        DataAttributeDto orderDto = DataAttributeDto.builder().name(StatisticType.DATE_ORDER.getDesc()).value(orderCount.toString())
                .unit(StatisticType.DATE_ORDER.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_ORDER.getCode().longValue()).build();
        result.add(orderDto);
        //实际充值
        BigDecimal factChargeAmount = chargeService.getTodayFactCharge(shopId);
        DataAttributeDto factChargeDto = DataAttributeDto.builder().name(StatisticType.DATE_RECHARGE_FACT.getDesc()).value(factChargeAmount.toPlainString())
                .unit(StatisticType.DATE_RECHARGE_FACT.getUnit()).url(RECHARGE_DATA).id(StatisticType.DATE_RECHARGE_FACT.getCode().longValue()).build();
        result.add(factChargeDto);
        //充值返赠
        BigDecimal giftChargeAmount = chargeService.getTodayGiftCharge(shopId);
        DataAttributeDto giftChargeDto = DataAttributeDto.builder().name(StatisticType.DATE_RECHARGE_GIFT.getDesc()).value(giftChargeAmount.toPlainString())
                .unit(StatisticType.DATE_RECHARGE_GIFT.getUnit()).url(RECHARGE_DATA).id(StatisticType.DATE_RECHARGE_GIFT.getCode().longValue()).build();
        result.add(giftChargeDto);
        //账面充值额
        BigDecimal accountChargeAmount = factChargeAmount.add(giftChargeAmount);
        DataAttributeDto accountChargeDto = DataAttributeDto.builder().name(StatisticType.DATE_ACCOUNT_RECHARGE.getDesc()).value(accountChargeAmount.toPlainString())
                .unit(StatisticType.DATE_ACCOUNT_RECHARGE.getUnit()).url(RECHARGE_DATA).id(StatisticType.DATE_ACCOUNT_RECHARGE.getCode().longValue()).build();
        result.add(accountChargeDto);
        //实际收款
        BigDecimal factConsumeAmount = customerConsumeService.getTodayFactConsumeAmount(shopId);
        DataAttributeDto factConsumeDto = DataAttributeDto.builder().name(StatisticType.DATE_CONSUME_FACT.getDesc()).value(factConsumeAmount.toPlainString())
                .unit(StatisticType.DATE_CONSUME_FACT.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_CONSUME_FACT.getCode().longValue()).build();
        result.add(factConsumeDto);
        //余额付款
        BigDecimal storeConsumeAmount = customerConsumeService.getTodayStoreConsumeAmount(shopId);
        DataAttributeDto storeConsumeDto = DataAttributeDto.builder().name(StatisticType.DATE_CONSUME_STORE.getDesc()).value(storeConsumeAmount.toPlainString())
                .unit(StatisticType.DATE_CONSUME_STORE.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_CONSUME_STORE.getCode().longValue()).build();
        result.add(storeConsumeDto);
        //账面营业额
        BigDecimal accountConsumeAmount = factConsumeAmount.add(storeConsumeAmount);
        DataAttributeDto accountConsumeDto = DataAttributeDto.builder().name(StatisticType.DATE_CONSUME.getDesc()).value(accountConsumeAmount.toPlainString())
                .unit(StatisticType.DATE_CONSUME.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_CONSUME.getCode().longValue()).build();
        result.add(accountConsumeDto);
        return result;
    }

    /**
     * 获取用户门店数据
     *
     * @param shopId
     * @return
     */
    @Cacheable(value = "userShopStatistics", unless = "#result == null", key = "T(String).valueOf(#shopId).concat(':').concat(#userId)")
    public List<DataAttributeDto> getUserStatistic(Long shopId, Long userId) {
        List<DataAttributeDto> result = new ArrayList<>();
        //预约量
        Integer appointmentCount = appointmentService.getTodayAppointmentCount(shopId, userId);
        DataAttributeDto appointmentDto = DataAttributeDto.builder().name(StatisticType.DATE_APPOINTMENT.getDesc()).value(appointmentCount.toString())
                .unit(StatisticType.DATE_APPOINTMENT.getUnit()).url(APPOINTMENT_DATA).id(StatisticType.DATE_APPOINTMENT.getCode().longValue()).build();
        result.add(appointmentDto);
        //订单量
        Integer orderCount = orderService.getTodayOrderCount(shopId, userId);
        DataAttributeDto orderDto = DataAttributeDto.builder().name(StatisticType.DATE_ORDER.getDesc()).value(orderCount.toString())
                .unit(StatisticType.DATE_ORDER.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_ORDER.getCode().longValue()).build();
        result.add(orderDto);
        //实际充值
        BigDecimal factChargeAmount = chargeService.getTodayFactCharge(shopId, userId);
        DataAttributeDto factChargeDto = DataAttributeDto.builder().name(StatisticType.DATE_RECHARGE_FACT.getDesc()).value(factChargeAmount.toPlainString())
                .unit(StatisticType.DATE_RECHARGE_FACT.getUnit()).url(RECHARGE_DATA).id(StatisticType.DATE_RECHARGE_FACT.getCode().longValue()).build();
        result.add(factChargeDto);
        //充值返赠
        BigDecimal giftChargeAmount = chargeService.getTodayGiftCharge(shopId, userId);
        DataAttributeDto giftChargeDto = DataAttributeDto.builder().name(StatisticType.DATE_RECHARGE_GIFT.getDesc()).value(giftChargeAmount.toPlainString())
                .unit(StatisticType.DATE_RECHARGE_GIFT.getUnit()).url(RECHARGE_DATA).id(StatisticType.DATE_RECHARGE_GIFT.getCode().longValue()).build();
        result.add(giftChargeDto);
        //账面充值额
        BigDecimal accountChargeAmount = factChargeAmount.add(giftChargeAmount);
        DataAttributeDto accountChargeDto = DataAttributeDto.builder().name(StatisticType.DATE_ACCOUNT_RECHARGE.getDesc()).value(accountChargeAmount.toPlainString())
                .unit(StatisticType.DATE_ACCOUNT_RECHARGE.getUnit()).url(RECHARGE_DATA).id(StatisticType.DATE_ACCOUNT_RECHARGE.getCode().longValue()).build();
        result.add(accountChargeDto);
        //实际收款
        BigDecimal factConsumeAmount = customerConsumeService.getTodayFactConsumeAmount(shopId, userId);
        DataAttributeDto factConsumeDto = DataAttributeDto.builder().name(StatisticType.DATE_CONSUME_FACT.getDesc()).value(factConsumeAmount.toPlainString())
                .unit(StatisticType.DATE_CONSUME_FACT.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_CONSUME_FACT.getCode().longValue()).build();
        result.add(factConsumeDto);
        //余额付款
        BigDecimal storeConsumeAmount = customerConsumeService.getTodayStoreConsumeAmount(shopId, userId);
        DataAttributeDto storeConsumeDto = DataAttributeDto.builder().name(StatisticType.DATE_CONSUME_STORE.getDesc()).value(storeConsumeAmount.toPlainString())
                .unit(StatisticType.DATE_CONSUME_STORE.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_CONSUME_STORE.getCode().longValue()).build();
        result.add(storeConsumeDto);
        //账面营业额
        BigDecimal accountConsumeAmount = factConsumeAmount.add(storeConsumeAmount);
        DataAttributeDto accountConsumeDto = DataAttributeDto.builder().name(StatisticType.DATE_CONSUME.getDesc()).value(accountConsumeAmount.toPlainString())
                .unit(StatisticType.DATE_CONSUME.getUnit()).url(ORDER_DATA).id(StatisticType.DATE_CONSUME.getCode().longValue()).build();
        result.add(accountConsumeDto);
        return result;
    }

    /**
     * 个人商户内累计指标
     *
     * @return
     */
    @Cacheable(value = "merchantUserKeyIndex", unless = "#result == null", key = "#userId")
    public UserKeyIndexDto getUserStatisticIndex(Long userId) {
        BigDecimal accumulativeRecharge = statisticDao.getStatisticSum(userId, StatisticType.DATE_RECHARGE_FACT);
        BigDecimal accumulativeService = statisticDao.getStatisticSum(userId, StatisticType.DATE_ORDER);
        BigDecimal accumulativeServiceAmount = statisticDao.getStatisticSum(userId, StatisticType.DATE_CONSUME);
        UserKeyIndexDto result = UserKeyIndexDto.builder().serviceTime(accumulativeService).rechargeAmount(accumulativeRecharge).serviceAmount(accumulativeServiceAmount).build();
        return result;
    }


    /**
     * 获取明细数据（今日以前的历史明细除记录表外其余数量与统计表可能不一致，由于记录状态变化导致）
     *
     * @param shopId
     * @param dataType
     * @param dateType
     * @param startDate
     * @param endDate
     * @return
     */
    public StatisticListDto getShopStatisticList(Long shopId, Integer dataType, String dateType, LocalDate startDate, LocalDate endDate, Integer pageIndex, Integer pageSize) {
        StatisticListDto result = new StatisticListDto();
        result.setPageIndex(pageIndex);
        result.setPageSize(pageSize);
        result.setTotalPage(0);
        StatisticType statisticType = StatisticType.getEnum(dataType).get();
        if (dateType.equals("day")) {
            IPage<Customer> customerPage;
            IPage<CustomerAppointment> appointmentPage;
            IPage<CustomerCharge> chargePage;
            IPage<Order> orderPage;
            switch (statisticType) {
                case DATE_VISIT:
                    List<CustomerDto> visitList = Lists.emptyList();
                    customerPage = getVisitCustomerList(shopId, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(customerPage.getRecords())) {
                        visitList = customerPage.getRecords().stream().map(customer -> BeanUtil.mapper(customer, CustomerDto.class)).collect(Collectors.toList());
                    }
                    result.setTotalPage(customerPage.getPages());
                    result.setCustomerList(visitList);
                    break;
                case DATE_CUSTOMER:
                    List<CustomerDto> customerList = Lists.emptyList();
                    customerPage = getNewCustomerList(shopId, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(customerPage.getRecords())) {
                        customerList = customerPage.getRecords().stream().map(customer -> BeanUtil.mapper(customer, CustomerDto.class)).collect(Collectors.toList());
                    }
                    result.setCustomerList(customerList);
                    break;
                case DATE_MEMBER_INCREASE:
                    List<CustomerDto> memberList = Lists.emptyList();
                    customerPage = getNewMemberList(shopId, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(customerPage.getRecords())) {
                        memberList = customerPage.getRecords().stream().map(customer -> BeanUtil.mapper(customer, CustomerDto.class)).collect(Collectors.toList());
                    }
                    result.setCustomerList(memberList);
                    break;
                case DATA_MEMBER_RECHARGE:
                    List<CustomerDto> reChargeList = Lists.emptyList();
                    customerPage = getRechargeList(shopId, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(customerPage.getRecords())) {
                        reChargeList = customerPage.getRecords().stream().map(customer -> BeanUtil.mapper(customer, CustomerDto.class)).collect(Collectors.toList());
                    }
                    result.setCustomerList(reChargeList);
                    break;
                case DATE_APPOINTMENT:
                    List<AppointmentDto> appointmentList = Lists.emptyList();
                    appointmentPage = getAppointmentList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(appointmentPage.getRecords())) {
                        appointmentList = appointmentPage.getRecords().stream().map(appointment -> BeanUtil.mapper(appointment, AppointmentDto.class)).collect(Collectors.toList());
                    }
                    result.setAppointmentList(appointmentList);
                    break;
                case DATE_ORDER:
                    List<OrderDto> orderList = Lists.emptyList();
                    orderPage = getOrderList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                        orderList = toOrderDto(orderPage.getRecords());
                    }
                    result.setOrderList(orderList);
                    break;
                case DATE_RECHARGE_FACT:
                    List<ChargeDto> factChargeList = Lists.emptyList();
                    chargePage = getChargeList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(chargePage.getRecords())) {
                        factChargeList = toChargeDto(chargePage.getRecords());
                    }
                    result.setChargeList(factChargeList);
                    break;
                case DATE_RECHARGE_GIFT:
                    List<ChargeDto> giftChargeList = Lists.emptyList();
                    chargePage = getChargeList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(chargePage.getRecords())) {
                        giftChargeList = toChargeDto(chargePage.getRecords());
                    }
                    result.setChargeList(giftChargeList);
                    break;
                case DATE_ACCOUNT_RECHARGE:
                    List<ChargeDto> chargeList = Lists.emptyList();
                    chargePage = getChargeList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(chargePage.getRecords())) {
                        chargeList = toChargeDto(chargePage.getRecords());
                    }
                    result.setChargeList(chargeList);
                    break;
                case DATE_CONSUME_FACT:
                    List<OrderDto> factConsumeList = Lists.emptyList();
                    orderPage = getFactConsumeList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                        factConsumeList = toOrderDto(orderPage.getRecords());
                    }
                    result.setOrderList(factConsumeList);
                    break;
                case DATE_CONSUME_STORE:
                    List<OrderDto> storeConsumeList = Lists.emptyList();
                    orderPage = getStoreConsumeList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                        storeConsumeList = toOrderDto(orderPage.getRecords());
                    }
                    result.setOrderList(storeConsumeList);
                    break;
                case DATE_CONSUME:
                    List<OrderDto> consumeList = Lists.emptyList();
                    orderPage = getOrderList(shopId, null, startDate, pageIndex, pageSize);
                    if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                        consumeList = toOrderDto(orderPage.getRecords());
                    }
                    result.setOrderList(consumeList);
                    break;
            }
        } else {//周，月，季度类型直接返回当日统计数据
            List<StatisticDataDto> statisticDataDtoList = getStatisticDateList(shopId, 0L, statisticType, startDate, endDate);
            result.setDataList(statisticDataDtoList);
        }

        return result;
    }

    /**
     * 获取日统计数据列表
     *
     * @param shopId
     * @param userId
     * @param dataType
     * @param startDate
     * @param endDate
     * @return
     */
    private List<StatisticDataDto> getStatisticDateList(Long shopId, Long userId, StatisticType dataType, LocalDate startDate, LocalDate endDate) {
        List<StatisticDataDto> result = Lists.emptyList();
        if (dataType.getCode() >= 1 && dataType.getCode() <= 3) {//用户和门店无关
            shopId = 0L;
        }
        List<Statistic> statistics = statisticDao.getStatisticDateList(shopId, userId, dataType, startDate, endDate);
        if (CollectionUtils.isNotEmpty(statistics)) {
            result = statistics.stream().map(statistic -> StatisticDataDto.builder().dataIndex(DateUtil.getChinese4LocalDateString(statistic.getDataDate()))
                    .dataValue(statistic.getDataValue()).build()).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取余额支付列表
     *
     * @param shopId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<Order> getStoreConsumeList(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        IPage<Order> result=new Page();
        List<Long> orderIds = customerConsumeService.getOrderIdsBy(shopId, date, ConsumeType.REMAINDER);
        if (CollectionUtils.isNotEmpty(orderIds)){
            result=orderService.getPageByOrderIds(orderIds, userId, pageIndex, pageSize);
        }
        return result;
    }

    /**
     * 获取实时支付订单列表
     *
     * @param shopId
     * @param userId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<Order> getFactConsumeList(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        //现在只有微信支付
        IPage<Order> result=new Page();
        List<Long> orderIds = customerConsumeService.getOrderIdsBy(shopId, date, ConsumeType.WX_PAY);
        if (CollectionUtils.isNotEmpty(orderIds)){
            result=orderService.getPageByOrderIds(orderIds, userId, pageIndex, pageSize);
        }
        return result;
    }

    /**
     * 获取充值列表(商户级别充值会比门店和个人的累计多)
     *
     * @param shopId
     * @param userId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<CustomerCharge> getChargeList(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return chargeService.getPageByShopAndDate(shopId, userId, date, pageIndex, pageSize);
    }

    /**
     * 获取订单列表
     *
     * @param shopId
     * @param userId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<Order> getOrderList(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return orderService.getPageByShopAndDate(shopId, userId, date, pageIndex, pageSize);
    }

    /**
     * 获取预约列表
     *
     * @param shopId
     * @param userId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<CustomerAppointment> getAppointmentList(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return appointmentService.getPageBy(shopId, userId, date, pageIndex, pageSize);
    }

    /**
     * 获取新充值会员列表
     *
     * @param shopId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<Customer> getRechargeList(Long shopId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerService.getRechargePage(shopId, date, pageIndex, pageSize);
    }

    /**
     * 获取新会员列表
     *
     * @param shopId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<Customer> getNewMemberList(Long shopId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerService.getNewMemberPage(date, pageIndex, pageSize);
    }

    /**
     * 获取日新增用户
     *
     * @param shopId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<Customer> getNewCustomerList(Long shopId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerService.getNewcustomerPage(date, pageIndex, pageSize);
    }

    /**
     * 获取日访问用户（昨日及之前不准确）
     *
     * @param shopId
     * @param date
     * @return
     */
    private IPage<Customer> getVisitCustomerList(Long shopId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerService.getLoginCustomer(date, pageIndex, pageSize);
    }


    /**
     * 获取明细数据（今日以前的历史明细除记录表外其余数量与统计表可能不一致，由于记录状态变化导致）
     *
     * @param shopId
     * @param dataType
     * @param dateType
     * @param startDate
     * @param endDate
     * @return
     */
    public StatisticListDto getUserStatisticList(Long shopId, Integer dataType, String dateType, LocalDate startDate, LocalDate endDate, Integer pageIndex, Integer pageSize) {
        Long userId = CurrentUserUtil.getMerchantUser().getId();
        StatisticListDto result = new StatisticListDto();
        result.setPageIndex(pageIndex);
        result.setPageSize(pageSize);
        result.setTotalPage(0);
        StatisticType statisticType = StatisticType.getEnum(dataType).get();
        IPage<CustomerAppointment> appointmentPage;
        IPage<Order> orderPage;
        IPage<CustomerCharge> chargePage;
        switch (statisticType) {
            case DATE_APPOINTMENT:
                List<AppointmentDto> appointmentList = Lists.emptyList();
                appointmentPage = getAppointmentList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(appointmentPage.getRecords())) {
                    appointmentList = appointmentPage.getRecords().stream().map(appointment -> BeanUtil.mapper(appointment, AppointmentDto.class)).collect(Collectors.toList());
                }
                result.setAppointmentList(appointmentList);
                break;
            case DATE_ORDER:
                List<OrderDto> orderList = Lists.emptyList();
                orderPage = getOrderList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                    orderList = toOrderDto(orderPage.getRecords());
                }
                result.setOrderList(orderList);
                break;
            case DATE_RECHARGE_FACT:
                List<ChargeDto> factChargeList = Lists.emptyList();
                chargePage = getChargeList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(chargePage.getRecords())) {
                    factChargeList = toChargeDto(chargePage.getRecords());
                }
                result.setChargeList(factChargeList);
                break;
            case DATE_RECHARGE_GIFT:
                List<ChargeDto> giftChargeList = Lists.emptyList();
                chargePage = getChargeList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(chargePage.getRecords())) {
                    giftChargeList = toChargeDto(chargePage.getRecords());
                }
                result.setChargeList(giftChargeList);
                break;
            case DATE_ACCOUNT_RECHARGE:
                List<ChargeDto> chargeList = Lists.emptyList();
                chargePage = getChargeList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(chargePage.getRecords())) {
                    chargeList = toChargeDto(chargePage.getRecords());
                }
                result.setChargeList(chargeList);
                break;
            case DATE_CONSUME_FACT:
                List<OrderDto> factConsumeList = Lists.emptyList();
                orderPage = getFactConsumeList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                    factConsumeList = toOrderDto(orderPage.getRecords());
                }
                result.setOrderList(factConsumeList);
                break;
            case DATE_CONSUME_STORE:
                List<OrderDto> storeConsumeList = Lists.emptyList();
                orderPage = getStoreConsumeList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                    storeConsumeList = toOrderDto(orderPage.getRecords());
                }
                result.setOrderList(storeConsumeList);
                break;
            case DATE_CONSUME:
                List<OrderDto> consumeList = Lists.emptyList();
                orderPage = getOrderList(shopId, userId, startDate, pageIndex, pageSize);
                if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
                    consumeList = toOrderDto(orderPage.getRecords());
                }
                result.setOrderList(consumeList);
                break;
        }
        return result;
    }

    /**
     * 支付对象转换
     *
     * @param charges
     * @return
     */
    private List<ChargeDto> toChargeDto(List<CustomerCharge> charges) {
        List<Long> customerIds = charges.stream().map(customerCharge -> customerCharge.getCustomerId()).collect(Collectors.toList());
        final Map<Long, Customer> customerMap = customerService.getListByIds(customerIds).stream().collect(Collectors.toMap(customer -> customer.getId(), customer -> customer));
        return charges.stream().map(charge -> {
            ChargeDto chargeDto = BeanUtil.mapper(charge, ChargeDto.class);
            Customer customer = customerMap.get(charge.getCustomerId());
            if (customer != null) {
                chargeDto.setCustomerName(customer.getName());
                chargeDto.setCustomerAvatar(customer.getAvatar());
            }
            return chargeDto;
        }).collect(Collectors.toList());
    }

    private List<OrderDto> toOrderDto(List<Order> orders) {
        return orders.stream().map(order -> {
            OrderDto dto = BeanUtil.mapper(order, OrderDto.class);
            StringBuilder stringBuilder = new StringBuilder().append(order.getGoodsNames()).append("共").append(order.getTotalNum()).append("件商品");
            dto.setOrderDesc(stringBuilder.toString());
            return dto;
        }).collect(Collectors.toList());
    }


    /**
     * 获取趋势图数据
     *
     * @param shopId
     * @param dataType
     * @param dateType
     * @param startDate
     * @param endDate
     * @return
     */
    public List<StatisticDataDto> getShopStatisticChart(Long shopId, Integer dataType, String dateType, LocalDate startDate, LocalDate endDate) {
        List<StatisticDataDto> result = Lists.emptyList();
        StatisticType statisticType = StatisticType.getEnum(dataType).get();
        if (dataType >= 1 && dataType <= 3) {//用户和门店无关
            shopId = 0L;
        }
        Integer endYear;
        Integer startYear;
        switch (dateType) {
            case "day":
                //显示过去7天的数据
                result = getStatisticDateList(shopId, 0L, statisticType, startDate.minusDays(7), startDate);
                break;
            case "weeks":
                //显示过去七周的数据
                List<StatisticWeekSum> weekSums;
                endYear = DateUtil.getWeekBasedYear(startDate);
                startYear = DateUtil.getWeekBasedYear(startDate.minusWeeks(7));
                Integer endWeek = DateUtil.getWeekOfWeekBasedYear(startDate);
                Integer startWeek = DateUtil.getWeekOfWeekBasedYear(startDate.minusWeeks(7));
                if (endYear.equals(startYear)) {//不跨年
                    weekSums = statisticWeekSumDao.getList(shopId, 0L, statisticType, endYear, startWeek, endWeek);
                } else {
                    weekSums = statisticWeekSumDao.getList(shopId, 0L, statisticType, startYear, startWeek, 52);
                    List<StatisticWeekSum> weekSums2 = statisticWeekSumDao.getList(shopId, 0L ,statisticType, endYear, 1, endWeek);
                    if (weekSums != null && weekSums2 != null) {
                        weekSums.addAll(weekSums2);
                    } else {
                        if (weekSums2 != null) {
                            weekSums = weekSums2;
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(weekSums)) {
                    result = weekSums.stream().map(statisticWeekSum -> StatisticDataDto.builder().dataIndex(statisticWeekSum.getWeek() + "周").dataValue(statisticWeekSum.getDataValue()).build())
                            .collect(Collectors.toList());
                }
                break;
            case "month":
                //显示过去6个月的数据
                List<StatisticMonthSum> monthSums;
                endYear = startDate.getYear();
                startYear = startDate.minusMonths(6).getYear();
                Integer startMonth = startDate.minusMonths(6).getMonthValue();
                Integer endMonth = startDate.getMonthValue();
                if (endYear.equals(startYear)) {//不跨年
                    monthSums = statisticMonthSumDao.getList(shopId, 0L, statisticType, endYear, startMonth, endMonth);
                } else {
                    monthSums = statisticMonthSumDao.getList(shopId, 0L, statisticType, startYear, startMonth, 12);
                    List<StatisticMonthSum> monthSums1 = statisticMonthSumDao.getList(shopId, 0L, statisticType, endYear, 1, endMonth);
                    if (monthSums != null && monthSums1 != null) {
                        monthSums.addAll(monthSums1);
                    } else {
                        if (monthSums1 != null) {
                            monthSums = monthSums1;
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(monthSums)) {
                    result = monthSums.stream().map(statisticMonthSum -> StatisticDataDto.builder().dataIndex(statisticMonthSum.getMonth() + "月").dataValue(statisticMonthSum.getDataValue()).build())
                            .collect(Collectors.toList());
                }
                break;
            case "quarter":
                //显示6个季度的数据
                List<StatisticQuarterSum> quarterSums;
                endYear = startDate.getYear();
                startYear = startDate.minusMonths(18).getYear();
                Integer startQuarter = DateUtil.getQuarterByLocalDate(startDate.minusMonths(18));
                Integer endQuarter = DateUtil.getQuarterByLocalDate(startDate);
                quarterSums = statisticQuarterSumDao.getList(shopId, 0L, statisticType, startYear, startQuarter, 4);
                List<StatisticQuarterSum> quarterSums1 = statisticQuarterSumDao.getList(shopId, 0L, statisticType, endYear, 1, endQuarter);
                if (quarterSums != null && quarterSums1 != null) {
                    quarterSums.addAll(quarterSums1);
                } else {
                    if (quarterSums1 != null) {
                        quarterSums = quarterSums1;
                    }
                }
                if (CollectionUtils.isNotEmpty(quarterSums)) {
                    result = quarterSums.stream().map(statisticQuarterSum -> StatisticDataDto.builder().dataIndex(statisticQuarterSum.getQuarter() + "季度").dataValue(statisticQuarterSum.getDataValue()).build())
                            .collect(Collectors.toList());
                }
                break;
        }
        return result;
    }


    /**
     * 获取个人趋势图数据
     *
     * @param shopId
     * @param dateType
     * @param startDate
     * @param endDate
     * @return
     */
    public List<StatisticDataDto> getUserStatisticChart(Long shopId, Integer dataType, String dateType, LocalDate startDate, LocalDate endDate) {
        Long userId = CurrentUserUtil.getMerchantUser().getId();
        List<StatisticDataDto> result = Lists.emptyList();
        StatisticType statisticType = StatisticType.getEnum(dataType).get();
        Integer endYear;
        Integer startYear;
        switch (dateType) {
            case "day":
                //显示过去7天的数据
                result = getStatisticDateList(shopId, userId, statisticType, startDate.minusDays(7), startDate);
                break;
            case "weeks":
                //显示过去七周的数据
                List<StatisticWeekSum> weekSums;
                endYear = DateUtil.getWeekBasedYear(startDate);
                startYear = DateUtil.getWeekBasedYear(startDate.minusWeeks(7));
                Integer endWeek = DateUtil.getWeekOfWeekBasedYear(startDate);
                Integer startWeek = DateUtil.getWeekOfWeekBasedYear(startDate.minusWeeks(7));
                if (endYear.equals(startYear)) {//不跨年
                    weekSums = statisticWeekSumDao.getList(shopId, userId, statisticType, endYear, startWeek, endWeek);
                } else {
                    weekSums = statisticWeekSumDao.getList(shopId, userId, statisticType, startYear, startWeek, 52);
                    List<StatisticWeekSum> weekSums2 = statisticWeekSumDao.getList(shopId, userId, statisticType, endYear, 1, endWeek);
                    if (weekSums != null && weekSums2 != null) {
                        weekSums.addAll(weekSums2);
                    } else {
                        if (weekSums2 != null) {
                            weekSums = weekSums2;
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(weekSums)) {
                    result = weekSums.stream().map(statisticWeekSum -> StatisticDataDto.builder().dataIndex(statisticWeekSum.getWeek() + "周").dataValue(statisticWeekSum.getDataValue()).build())
                            .collect(Collectors.toList());
                }
                break;
            case "month":
                //显示过去6个月的数据
                List<StatisticMonthSum> monthSums;
                endYear = startDate.getYear();
                startYear = startDate.minusMonths(6).getYear();
                Integer startMonth = startDate.minusMonths(6).getMonthValue();
                Integer endMonth = startDate.getMonthValue();
                if (endYear.equals(startYear)) {//不跨年
                    monthSums = statisticMonthSumDao.getList(shopId, userId, statisticType, endYear, startMonth, endMonth);
                } else {
                    monthSums = statisticMonthSumDao.getList(shopId, userId, statisticType, startYear, startMonth, 12);
                    List<StatisticMonthSum> monthSums1 = statisticMonthSumDao.getList(shopId, userId, statisticType, endYear, 1, endMonth);
                    if (monthSums != null && monthSums1 != null) {
                        monthSums.addAll(monthSums1);
                    } else {
                        if (monthSums1 != null) {
                            monthSums = monthSums1;
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(monthSums)) {
                    result = monthSums.stream().map(statisticMonthSum -> StatisticDataDto.builder().dataIndex(statisticMonthSum.getMonth() + "月").dataValue(statisticMonthSum.getDataValue()).build())
                            .collect(Collectors.toList());
                }
                break;
            case "quarter":
                //显示6个季度的数据
                List<StatisticQuarterSum> quarterSums;
                endYear = startDate.getYear();
                startYear = startDate.minusMonths(18).getYear();
                Integer startQuarter = DateUtil.getQuarterByLocalDate(startDate.minusMonths(18));
                Integer endQuarter = DateUtil.getQuarterByLocalDate(startDate);
                quarterSums = statisticQuarterSumDao.getList(shopId, userId, statisticType, startYear, startQuarter, 4);
                List<StatisticQuarterSum> quarterSums1 = statisticQuarterSumDao.getList(shopId, userId, statisticType, endYear, 1, endQuarter);
                if (quarterSums != null && quarterSums1 != null) {
                    quarterSums.addAll(quarterSums1);
                } else {
                    if (quarterSums1 != null) {
                        quarterSums = quarterSums1;
                    }
                }
                if (CollectionUtils.isNotEmpty(quarterSums)) {
                    result = quarterSums.stream().map(statisticQuarterSum -> StatisticDataDto.builder().dataIndex(statisticQuarterSum.getQuarter() + "季度").dataValue(statisticQuarterSum.getDataValue()).build())
                            .collect(Collectors.toList());
                }
                break;
        }
        return result;
    }


    /**
     * 排名列表
     * @param shopId
     * @param dataType
     * @param dateType
     * @param startDate
     * @param endDate
     * @return
     */
    public List<RankDto> getRankList(Long shopId, Integer dataType, String dateType, LocalDate startDate, LocalDate endDate) {
        List<RankDto> result=Lists.emptyList();
        LocalDate today=LocalDate.now();
        StatisticType statisticType=StatisticType.getEnum(dataType).get();
        switch (dateType){
            case "day":
                if (startDate.equals(today)){//今日
                    result=getTodayRankList(shopId,statisticType);
                }else{//过去日期
                    result=getDateRankList(shopId,statisticType,startDate);
                }
                break;
            case "weeks":
                if (DateUtil.ifSameWeek(startDate,today)){//本周
                    result=getRecentPeriodRankList(shopId,statisticType,DateUtil.getWeekBasedYear(today),null,null,DateUtil.getWeekOfWeekBasedYear(today));
                }else{//过去周
                    result=getWeekRankList(shopId,statisticType,DateUtil.getWeekBasedYear(startDate),DateUtil.getWeekOfWeekBasedYear(startDate));
                }
                break;
            case "month":
                if (DateUtil.ifSameMonth(startDate,today)){//本月
                    result=getRecentPeriodRankList(shopId,statisticType,today.getYear(),null,today.getMonthValue(),null);
                }else{//过去月
                    result=getMonthRankList(shopId,statisticType,startDate.getYear(),startDate.getMonthValue());
                }
                break;
            case "quarter":
                if (DateUtil.ifSameQuarter(startDate,today)){//本季度
                    result=getRecentPeriodRankList(shopId,statisticType,today.getYear(),DateUtil.getQuarterByLocalDate(today),null,null);
                }else{//过去季度
                    result=getQuarterRankList(shopId,statisticType,startDate.getYear(),DateUtil.getQuarterByLocalDate(startDate));
                }
                break;
        }

        //赋予userName排名rank
        if (CollectionUtils.isNotEmpty(result)){
            List<Long> userIds=new ArrayList<>();
            Set<BigDecimal> values=new LinkedHashSet<>();
            result=result.stream().sorted(Comparator.comparing(RankDto::getValue).reversed()).map(rankDto -> {
                rankDto.setValue(rankDto.getValue().setScale(0,BigDecimal.ROUND_HALF_UP));
                userIds.add(rankDto.getMerchantUserId());
                values.add(rankDto.getValue());
                return rankDto;
            }).collect(Collectors.toList());

            Map<Long,MerchantUser> userMap=merchantUserService.getMerchantUserByIds(userIds).stream().collect(Collectors.toMap(user->user.getId(),user->user));
            Map<BigDecimal,Integer> rankMap=new HashMap<>();
            List<BigDecimal> valueList=new ArrayList<>(values);
            for (int i=0;i<valueList.size();i++){
                rankMap.put(valueList.get(i),i+1);
            }

            //
            result.stream().forEach(rankDto -> {
                MerchantUser merchantUser=userMap.get(rankDto.getMerchantUserId());
                if (merchantUser!=null){
                    rankDto.setMerchantUserName(merchantUser.getName());
                }
                rankDto.setRank(rankMap.get(rankDto.getValue()));
            });
        }
        return result;
    }

    /**
     * 获取近期的绩效数据
     * @param shopId
     * @param statisticType
     * @param year
     * @param quarter
     * @param month
     * @param week
     * @return
     */
    private List<RankDto> getRecentPeriodRankList(Long shopId, StatisticType statisticType,Integer year,Integer quarter,Integer month,Integer week) {
        List<RankDto> result=Lists.emptyList();
        switch (statisticType){
            case EVALUATE:
                result=getPeriodAverageRankList(shopId,statisticType,year,quarter,month,week);
                break;
            case DATE_RECHARGE_FACT:
                result=getPeriodSumRankList(shopId,statisticType,year,quarter,month,week);
                break;
        }
        return result;
    }

    /**
     * 获取期间合计数据
     * @param shopId
     * @param statisticType
     * @param year
     * @param quarter
     * @param month
     * @param week
     * @return
     */
    private List<RankDto> getPeriodSumRankList(Long shopId, StatisticType statisticType, Integer year, Integer quarter, Integer month, Integer week) {
        List<RankDto> result=Lists.emptyList();
        List<Statistic> statistics=statisticDao.getSumListBy(shopId,statisticType.getCode(),year,quarter,month,week);
        if (CollectionUtils.isNotEmpty(statistics)){
            result=statistics.stream().filter(statistic -> statistic.getMerchantUserId()>0).map(statistic -> RankDto.builder().merchantUserId(statistic.getMerchantUserId()).value(statistic.getDataValue()).build())
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取季度汇总排名
     * @param shopId
     * @param statisticType
     * @param year
     * @param quarter
     * @return
     */
    private List<RankDto> getQuarterRankList(Long shopId, StatisticType statisticType, int year, int quarter) {
        List<RankDto> result=Lists.emptyList();
        List<StatisticQuarterSum> quarterSums=statisticQuarterSumDao.getList(shopId,null,statisticType,year,quarter,quarter);
        if (CollectionUtils.isNotEmpty(quarterSums)){
            result=quarterSums.stream().filter(statisticQuarterSum -> statisticQuarterSum.getMerchantUserId()>0).map(statistic -> RankDto.builder().merchantUserId(statistic.getMerchantUserId()).value(statistic.getDataValue()).build())
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取月汇总排名
     * @param shopId
     * @param statisticType
     * @param year
     * @param month
     * @return
     */
    private List<RankDto> getMonthRankList(Long shopId, StatisticType statisticType, int year, int month) {
        List<RankDto> result=Lists.emptyList();
        List<StatisticMonthSum> monthSums=statisticMonthSumDao.getList(shopId,null,statisticType,year,null,month);
        if (CollectionUtils.isNotEmpty(monthSums)){
            result=monthSums.stream().filter(statisticMonthSum -> statisticMonthSum.getMerchantUserId()>0).map(statistic -> RankDto.builder().merchantUserId(statistic.getMerchantUserId()).value(statistic.getDataValue()).build())
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取周汇总排名
     * @param shopId
     * @param statisticType
     * @param year
     * @param week
     * @return
     */
    private List<RankDto> getWeekRankList(Long shopId, StatisticType statisticType, int year, int week) {
        List<RankDto> result=Lists.emptyList();
        List<StatisticWeekSum> weekSums=statisticWeekSumDao.getList(shopId,null,statisticType,year,week,week);
        if (CollectionUtils.isNotEmpty(weekSums)){
            result=weekSums.stream().filter(statisticWeekSum -> statisticWeekSum.getMerchantUserId()>0).map(statistic -> RankDto.builder().merchantUserId(statistic.getMerchantUserId()).value(statistic.getDataValue()).build())
                    .collect(Collectors.toList());
        }
        return result;
    }


    /**
     * 获取区间平均数据排名
     * @param shopId
     * @param statisticType
     * @param year
     * @param month
     * @param week
     * @return
     */
    private List<RankDto> getPeriodAverageRankList(Long shopId, StatisticType statisticType,Integer year,Integer quarter,Integer month,Integer week) {
        List<RankDto> result=Lists.emptyList();
        List<Statistic> statistics=statisticDao.getAverageListBy(shopId,statisticType.getCode(),year,quarter,month,week);
        if (CollectionUtils.isNotEmpty(statistics)){
            result=statistics.stream().filter(statistic -> statistic.getMerchantUserId()>0).map(statistic -> RankDto.builder().merchantUserId(statistic.getMerchantUserId()).value(statistic.getDataValue()).build())
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取日排名
     * @param shopId
     * @param statisticType
     * @param startDate
     * @return
     */
    private List<RankDto> getDateRankList(Long shopId, StatisticType statisticType, LocalDate startDate) {
        List<RankDto> result=Lists.emptyList();
        List<Statistic> statistics=statisticDao.getStatisticDateList(shopId,null,statisticType,startDate,startDate);
        if (CollectionUtils.isNotEmpty(statistics)){
            result=statistics.stream().filter(Statistic -> Statistic.getMerchantUserId()>0).map(statistic -> RankDto.builder().merchantUserId(statistic.getMerchantUserId()).value(statistic.getDataValue()).build())
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取今日排名
     * @param shopId
     * @param statisticType
     * @return
     */
    private List<RankDto> getTodayRankList(Long shopId, StatisticType statisticType) {
        List<RankDto> result=Lists.emptyList();
        switch (statisticType){
            case EVALUATE:
                List<MerchantUser> merchantUsers=merchantUserService.getByShopId(shopId);
                if (CollectionUtils.isNotEmpty(merchantUsers)){
                    result=merchantUsers.stream().map(merchantUser ->
                            RankDto.builder().merchantUserId(merchantUser.getId()).value(BigDecimal.valueOf(merchantUser.getGrade().longValue())).build())
                            .collect(Collectors.toList());
                }
                break;
            case DATE_RECHARGE_FACT:
                List<CustomerCharge> customerCharges=chargeService.getSumByShopId(shopId,LocalDate.now());
                Map<Long,CustomerCharge> customerChargeMap=new HashMap<>();
                if (CollectionUtils.isNotEmpty(customerCharges)){
                    customerChargeMap=customerCharges.stream().collect(Collectors.toMap(customerCharge->customerCharge.getMerchantUserId(),customerCharge->customerCharge));
                }
                //考虑部分人销售为0的情况
                List<Long> merchantUserIds=merchantUserService.getMerchantUsersIdsByShopId(shopId);
                result=new ArrayList<>();
                if (CollectionUtils.isNotEmpty(merchantUserIds)){
                    for(Long userId:merchantUserIds){
                        RankDto rankDto;
                        CustomerCharge customerCharge=customerChargeMap.get(userId);
                        if (customerCharge!=null){
                            rankDto=RankDto.builder().merchantUserId(userId).value(customerCharge.getAmount()).build();
                        }else{
                            rankDto=RankDto.builder().merchantUserId(userId).value(BigDecimal.ZERO).build();
                        }
                        result.add(rankDto);
                    }
                }
                break;
        }
        return result;
    }

}
