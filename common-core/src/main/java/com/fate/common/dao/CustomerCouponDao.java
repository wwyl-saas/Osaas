package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.enums.CouponStatus;
import com.fate.common.enums.CouponType;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * C端用户卡券表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerCouponDao extends IService<CustomerCoupon> {

    List<CustomerCoupon> selectByStatusAndCustomerId(Long customerId, List<CouponStatus> couponStatuses);

    List<CustomerCoupon> findByCustomerId(Long id);

    List<CustomerCoupon> selectByStatusAndTypeAndCustomerId(Long customerId, ArrayList<CouponStatus> couponStatuses, ArrayList<CouponType> couponTypes);

    List<CustomerCoupon> getByCouponIds(List<Long> expiredCouponIds);
}
