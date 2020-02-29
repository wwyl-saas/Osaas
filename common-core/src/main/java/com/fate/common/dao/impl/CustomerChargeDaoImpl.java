package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerChargeDao;
import com.fate.common.entity.CustomerCharge;
import com.fate.common.enums.ChargeStatus;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.CustomerChargeMapper;
import com.fate.common.model.StatisticModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 会员充值表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-14
 */
@Repository
public class CustomerChargeDaoImpl extends ServiceImpl<CustomerChargeMapper, CustomerCharge> implements CustomerChargeDao {

    @Override
    public List<CustomerCharge> selectByCustomerId(Long customerId) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<CustomerCharge>().eq(CustomerCharge.CUSTOMER_ID,customerId).eq(CustomerCharge.CHARGE_STATUS, ChargeStatus.COMPLETE).orderByDesc(CustomerCharge.CREATE_TIME));
    }

    @Override
    public Integer getNewChargeCountBy(Long shopId, LocalDate now) {
        QueryWrapper queryWrapper=new QueryWrapper<CustomerCharge>().eq(CustomerCharge.CHARGE_STATUS,ChargeStatus.COMPLETE);
        if (shopId!=null){
            queryWrapper.eq(CustomerCharge.SHOP_ID,shopId);
        }
        queryWrapper.between(CustomerCharge.UPDATE_TIME,now.atTime(LocalTime.MIN),now.atTime(23,59,59));
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public BigDecimal getFactCharge(Long shopId, LocalDate now, Long userId) {
        return baseMapper.getFactCharge(shopId,now.atTime(LocalTime.MIN),now.atTime(23,59,59),userId);
    }

    @Override
    public BigDecimal getTodayGiftCharge(Long shopId, LocalDate now, Long userId) {
        return baseMapper.getTodayGiftCharge(shopId,now.atTime(LocalTime.MIN),now.atTime(23,59,59),userId);
    }

    @Override
    public IPage<CustomerCharge> getPageBy(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        QueryWrapper queryWrapper=new QueryWrapper<CustomerCharge>().between(CustomerCharge.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
        if (shopId!=null){
            queryWrapper.eq(CustomerCharge.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(CustomerCharge.MERCHANT_USER_ID,userId);
        }
        queryWrapper.orderByDesc(CustomerCharge.CREATE_TIME);
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public List<Long> getCustomerIdsBy(Long shopId, LocalDate date) {
        return baseMapper.getCustomerIdsBy(shopId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public List<CustomerCharge> getGroupSumByShopId(Long shopId, LocalDate date) {
        return baseMapper.getGroupSumByShopId(shopId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public List<StatisticModel> getChargeCount(Long merchantId, LocalDate date) {
        return baseMapper.getChargeCount(merchantId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public List<StatisticModel> getChargeAmount(Long merchantId, LocalDate date) {
        return baseMapper.getChargeAmount(merchantId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public List<StatisticModel> getFactChargeAmount(Long merchantId, LocalDate date) {
        return baseMapper.getFactChargeAmount(merchantId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public List<StatisticModel> getGiftChargeAmount(Long merchantId, LocalDate date) {
        return baseMapper.getGiftChargeAmount(merchantId,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
    }

    @Override
    public IPage<CustomerCharge> getPageByShopIds(List<Long> list) {
        QueryWrapper queryWrapper=new QueryWrapper<CustomerCharge>().in(CustomerCharge.SHOP_ID,list);
        queryWrapper.orderByAsc(CustomerCharge.UPDATE_TIME);
        return baseMapper.getPageByShopIds(queryWrapper);

    }
    @Override
    public IPage<CustomerCharge> getPageByMerchant(Integer pageIndex, Integer pageSize) {
        QueryWrapper queryWrapper=new QueryWrapper<CustomerCharge>();
        queryWrapper.orderByAsc(CustomerCharge.UPDATE_TIME);
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);

    }


}
