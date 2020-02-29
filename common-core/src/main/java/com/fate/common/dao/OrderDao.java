package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Order;
import com.fate.common.enums.OrderStatus;
import com.fate.common.model.StatisticModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface OrderDao extends IService<Order> {
    IPage<Order> findByCustomer(Long customerId,OrderStatus status,Long startPage,Long pageSize);

    IPage<Order> findAll(Long startPage,Long pageSize);

    IPage<Order> getByShopPage(Long shopId, Long pageIndex, Long pageSize);

    IPage<Order> getByShopAndBetweenDatePage(Long shopId, LocalDateTime start, LocalDateTime end, Long pageIndex, Long pageSize);

    IPage<Order> getByShopAndUserPage(Long shopId, Long userId, Long pageIndex, Long pageSize);

    IPage<Order> getByShopAndUserAndCustomerAndDatePage(Long shopId, Long userId,OrderStatus orderStatus,Long customerId, LocalDate date, Long pageIndex, Long pageSize);

    Integer getTodayNewSettleCount(Long shopId, Long userId, LocalDate now);

    Integer getCountByShopStatusAndCustomer(Long shopId, Long customerId, Long userId, OrderStatus status, LocalDate date);

    IPage<Order> getOrderByStatusAndOrderNoAndIdPage(Long orderId, String orderNo, Integer orderStatus, Long pageIndex, Long pageSize);

    Integer getOrderCountBy(Long shopId, LocalDate date, Long userId);

    BigDecimal getFactConsumeAmount(Long shopId, LocalDate now);

    List<Long> getOrderIdsByShopAndUserAndDate(Long shopId, Long userId, LocalDate date);

    IPage<Order> getPageByOrderIds(List<Long> orderIds, Long userId, Integer pageIndex, Integer pageSize);

    IPage<Order> getPageBy(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize);

    List<Order> getByStatusAndCreateTimeBefore(OrderStatus status, LocalDateTime dateTime, Long merchantId);

    List<StatisticModel> getOrderCount(Long merchantId, LocalDate date);
}
