package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerConsumeDao;
import com.fate.common.entity.CustomerCharge;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.enums.ConsumeType;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.CustomerConsumeMapper;
import com.fate.common.model.StatisticModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 会员充值消费记录表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class CustomerConsumeDaoImpl extends ServiceImpl<CustomerConsumeMapper, CustomerConsume> implements CustomerConsumeDao {


    @Override
    public List<CustomerConsume> selectByCustomerAndType(Long customerId, Integer type) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        QueryWrapper queryWrapper=new QueryWrapper<CustomerConsume>().eq(CustomerConsume.CUSTOMER_ID,customerId);
        if (type!=null){
            queryWrapper.eq(CustomerConsume.CONSUME_TYPE,type);
        }
        queryWrapper.orderByDesc(CustomerConsume.CREATE_TIME);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<CustomerConsume> selectByCustomerId(Long customerId) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<CustomerConsume>().eq(CustomerConsume.CUSTOMER_ID,customerId).orderByDesc(CustomerConsume.CREATE_TIME));
    }

    @Override
    public CustomerConsume selectByOrderId(Long orderId) {
        return baseMapper.selectOne(new QueryWrapper<CustomerConsume>().eq(CustomerConsume.ORDER_ID,orderId));
    }

    @Override
    public BigDecimal getFactConsumeAmount(Long shopId, LocalDate date) {
        return baseMapper.getFactConsumeAmount(shopId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public BigDecimal getStoreConsumeAmount(Long shopId, LocalDate date) {
        return baseMapper.getStoreConsumeAmount(shopId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public BigDecimal getFactConsumeAmountByOrderIds(Long shopId, List<Long> orderIds) {
        Assert.notEmpty(orderIds,ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getFactConsumeAmountByOrderIds(shopId,orderIds);
    }

    @Override
    public BigDecimal getStoreConsumeAmountByOrderIds(Long shopId, List<Long> orderIds) {
        Assert.notEmpty(orderIds,ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getStoreConsumeAmountByOrderIds(shopId,orderIds);
    }

    @Override
    public List<Long> getOrderIdsBy(Long shopId, LocalDate date, ConsumeType consumeType) {
        return baseMapper.getOrderIdsBy(shopId,consumeType.getCode(),date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public BigDecimal getConsumeAmountByCustomerId(Long customerId) {
        return baseMapper.getConsumeAmountByCustomerId(customerId);
    }


    @Override
    public CustomerConsume getByIdAndMerchantId(Long id, Long merchantId) {
        return baseMapper.selectOne(new QueryWrapper<CustomerConsume>().eq(CustomerConsume.ID,id).eq(CustomerConsume.MERCHANT_ID,merchantId));
    }

    @Override
    public List<StatisticModel> getConsumeAmount(Long merchantId, LocalDate date, ConsumeType type) {
        return baseMapper.getConsumeAmount(merchantId,date.atTime(LocalTime.MIN),date.atTime(23,59,59),type!=null?type.getCode():null);
    }

    @Override
    public BigDecimal getTodayFactConsumeAmount(Long shopId, Long userId, LocalDate date) {
        return baseMapper.getTodayFactConsumeAmount(shopId,userId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public BigDecimal getTodayStoreConsumeAmount(Long shopId, Long userId, LocalDate date) {
        return baseMapper.getTodayStoreConsumeAmount(shopId,userId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public IPage<CustomerConsume> getPageByMerchant(Long pageIndex, Long pageSize) {
        Page<CustomerConsume> page=new Page<>(pageIndex,pageSize) ;
        QueryWrapper queryWrapper=new QueryWrapper<CustomerConsume>();
        queryWrapper.orderByAsc(CustomerConsume.CREATE_TIME);
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public IPage<CustomerConsume> getPageByShopIds(Long pageIndex, Long pageSize, List<Long> list) {
        Page<CustomerConsume> page=new Page<>(pageIndex,pageSize) ;
        QueryWrapper queryWrapper=new QueryWrapper<CustomerConsume>().in(CustomerCharge.SHOP_ID,list);
        queryWrapper.orderByAsc(CustomerConsume.CREATE_TIME);
        return baseMapper.selectPage(page,queryWrapper);
    }






}
