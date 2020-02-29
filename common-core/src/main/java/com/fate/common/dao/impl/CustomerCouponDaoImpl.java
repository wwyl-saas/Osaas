package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerCouponDao;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.enums.CouponStatus;
import com.fate.common.enums.CouponType;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.CustomerCouponMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * C端用户卡券表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class CustomerCouponDaoImpl extends ServiceImpl<CustomerCouponMapper, CustomerCoupon> implements CustomerCouponDao {

    @Override
    public List<CustomerCoupon> selectByStatusAndCustomerId(Long customerId, List<CouponStatus> couponStatuses) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        Assert.notEmpty(couponStatuses, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<CustomerCoupon>().eq(CustomerCoupon.CUSTOMER_ID,customerId).in(CustomerCoupon.STATUS,couponStatuses));
    }

    @Override
    public List<CustomerCoupon> findByCustomerId(Long id) {
        return baseMapper.selectList(new QueryWrapper<CustomerCoupon>().eq(CustomerCoupon.CUSTOMER_ID,id).ne(CustomerCoupon.STATUS,CouponStatus.USED));
    }

    @Override
    public List<CustomerCoupon> selectByStatusAndTypeAndCustomerId(Long customerId, ArrayList<CouponStatus> couponStatuses, ArrayList<CouponType> couponTypes) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        Assert.notEmpty(couponStatuses, ResponseInfo.PARAM_NULL.getMsg());
        Assert.notEmpty(couponTypes, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<CustomerCoupon>().eq(CustomerCoupon.CUSTOMER_ID,customerId).in(CustomerCoupon.STATUS,couponStatuses).in(CustomerCoupon.TYPE,couponTypes));
    }

    @Override
    public List<CustomerCoupon> getByCouponIds(List<Long> expiredCouponIds) {
        return baseMapper.selectList(new QueryWrapper<CustomerCoupon>().in(CustomerCoupon.COUPON_ID,expiredCouponIds).eq(CustomerCoupon.STATUS, CouponStatus.NOT_USED));
    }


}
