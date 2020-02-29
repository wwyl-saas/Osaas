package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.OrderDao;
import com.fate.common.entity.Order;
import com.fate.common.enums.OrderStatus;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.OrderMapper;
import com.fate.common.model.StatisticModel;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class OrderDaoImpl extends ServiceImpl<OrderMapper, Order> implements OrderDao {

    @Override
    public IPage<Order> findByCustomer(Long customerId, OrderStatus status,Long startPage,Long pageSize) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().eq(Order.CUSTOMER_ID,customerId);
        if(status!=null){
            queryWrapper=queryWrapper.eq(Order.STATUS,status.getCode());
        }
        queryWrapper.orderByDesc(Order.UPDATE_TIME);
        return this.baseMapper.selectPage(new Page<Order>(startPage,pageSize),queryWrapper);
    }

    @Override
    public IPage<Order> findAll(Long startPage,Long pageSize) {
        return baseMapper.selectPage(new Page<>(startPage,pageSize),new QueryWrapper<>());
    }

    @Override
    public IPage<Order> getByShopPage(Long shopId, Long pageIndex, Long pageSize) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        return this.baseMapper.selectPage(new Page<>(pageIndex,pageSize),new QueryWrapper<Order>().eq(Order.SHOP_ID,shopId));
    }

    @Override
    public IPage<Order> getByShopAndBetweenDatePage(Long shopId, LocalDateTime start, LocalDateTime end, Long pageIndex, Long pageSize) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        return this.baseMapper.selectPage(new Page<>(pageIndex,pageSize),new QueryWrapper<Order>().eq(Order.SHOP_ID,shopId).between(Order.CREATE_TIME,start,end));
    }

    @Override
    public IPage<Order> getByShopAndUserPage(Long shopId, Long userId, Long pageIndex, Long pageSize) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        QueryWrapper queryWrapper;
        if (userId!=null){
            queryWrapper= new QueryWrapper<Order>()
                    .nested(i -> i.eq(Order.MERCHANT_USER_ID, userId).or().eq(Order.CHECKER, userId))
                    .and(i -> i.ge(Order.SHOP_ID, shopId));
        }else{
            queryWrapper= new QueryWrapper<Order>().eq(Order.SHOP_ID,shopId);
        }
        queryWrapper.last("order by create_time desc");
        return this.baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public IPage<Order> getByShopAndUserAndCustomerAndDatePage(Long shopId, Long userId, OrderStatus status,Long customerId,LocalDate date,Long pageIndex, Long pageSize) {
        QueryWrapper queryWrapper= new QueryWrapper<Order>();
        if (shopId!=null){
            queryWrapper.eq(Order.SHOP_ID,shopId);
        }
        if (customerId!=null){
            queryWrapper.eq(Order.CUSTOMER_ID,customerId);
        }
        if (userId!=null){
            queryWrapper.eq(Order.MERCHANT_USER_ID,userId);
        }
        if (status!=null){
            queryWrapper.eq(Order.STATUS,status);
        }
        if (date!=null){
            queryWrapper.between(Order.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
        }
        queryWrapper.last("order by create_time desc");
        return this.baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public Integer getTodayNewSettleCount(Long shopId, Long userId, LocalDate date) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        QueryWrapper queryWrapper;
        if (userId!=null){
            queryWrapper= new QueryWrapper<Order>()
                    .nested(i -> i.eq(Order.MERCHANT_USER_ID, userId).or().eq(Order.CHECKER, userId))
                    .and(i -> i.ge(Order.SHOP_ID, shopId)).and(i -> i.between(Order.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)));
        }else{
            queryWrapper= new QueryWrapper<Order>().eq(Order.SHOP_ID,shopId).between(Order.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
        }
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer getCountByShopStatusAndCustomer(Long shopId, Long customerId, Long userId, OrderStatus status, LocalDate date) {
        QueryWrapper queryWrapper = new QueryWrapper<Order>();
        if (shopId != null) {
            queryWrapper.eq(Order.SHOP_ID, shopId);
        }
        if (customerId != null) {
            queryWrapper.eq(Order.CUSTOMER_ID, customerId);
        }
        if (userId != null) {
            queryWrapper.eq(Order.MERCHANT_USER_ID, userId);
        }
        if (status != null) {
            queryWrapper.eq(Order.STATUS, status);
        }
        if (date != null) {
            queryWrapper.between(Order.CREATE_TIME, date.atTime(LocalTime.MIN), date.atTime(23,59,59));
        }
        return baseMapper.selectCount(queryWrapper);
    }


    public IPage<Order> getOrderByStatusAndOrderNoAndIdPage(Long orderId, String orderNo, Integer orderStatus, Long pageIndex, Long pageSize) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();
        if (null!=orderId){
            queryWrapper.eq(Order.ID,orderId);
        }
        if (null!=orderNo && !orderNo.equals("")){
            queryWrapper.eq(Order.ORDER_NO,orderNo);
        }
        if(null!=orderStatus){
            queryWrapper.eq(Order.STATUS,orderStatus);
        }
        queryWrapper.last("order by "+Order.CREATE_TIME);
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public Integer getOrderCountBy(Long shopId, LocalDate date, Long userId) {
        QueryWrapper queryWrapper=new QueryWrapper<Order>().between(Order.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
        queryWrapper.in(Order.STATUS,Lists.list(OrderStatus.COMPLETED,OrderStatus.PAYED));
        if (shopId!=null){
            queryWrapper.eq(Order.SHOP_ID,shopId);
        }if (userId!=null){
            queryWrapper.eq(Order.MERCHANT_USER_ID,userId);
        }
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public BigDecimal getFactConsumeAmount(Long shopId, LocalDate date) {
        return baseMapper.getFactConsumeAmount(shopId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public List<Long> getOrderIdsByShopAndUserAndDate(Long shopId, Long userId, LocalDate date) {
        return baseMapper.getOrderIdsByShopAndUserAndDate(shopId,userId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public IPage<Order> getPageByOrderIds(List<Long> orderIds, Long userId, Integer pageIndex, Integer pageSize) {
        Assert.notEmpty(orderIds,ResponseInfo.PARAM_NULL.getMsg());
        QueryWrapper queryWrapper=new QueryWrapper<Order>().in(Order.ID,orderIds)
                .in(Order.STATUS,Lists.list(OrderStatus.COMPLETED,OrderStatus.PAYED));
        if (userId!=null){
            queryWrapper.eq(Order.MERCHANT_USER_ID,userId);
        }
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public IPage<Order> getPageBy(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        QueryWrapper queryWrapper=new QueryWrapper<Order>().between(Order.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59))
                .in(Order.STATUS,Lists.list(OrderStatus.COMPLETED,OrderStatus.PAYED));
        if (shopId!=null){
            queryWrapper.eq(Order.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(Order.MERCHANT_USER_ID,userId);
        }
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public List<Order> getByStatusAndCreateTimeBefore(OrderStatus status, LocalDateTime dateTime, Long merchantId) {
        return baseMapper.selectList(new QueryWrapper<Order>().eq(Order.STATUS,status).lt(Order.UPDATE_TIME, dateTime).eq(Order.MERCHANT_ID,merchantId));
    }

    @Override
    public List<StatisticModel> getOrderCount(Long merchantId, LocalDate date) {
        return baseMapper.getOrderCount(merchantId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

}
