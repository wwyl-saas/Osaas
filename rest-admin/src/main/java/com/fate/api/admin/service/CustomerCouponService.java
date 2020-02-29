package com.fate.api.admin.service;

import com.fate.common.dao.CustomerConsumeDao;
import com.fate.common.dao.CustomerCouponDao;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.entity.CustomerCoupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: parent
 * @description: 优惠券
 * @author: chenyixin
 * @create: 2019-09-25 16:45
 **/
@Service
@Slf4j
public class CustomerCouponService {
    @Resource
    CustomerCouponDao customerCouponDao;

    public List<CustomerCoupon> getByCouponIds(List<Long> expiredCouponIds) {
        return customerCouponDao.getByCouponIds(expiredCouponIds);
    }

    public void updateBatch(List<CustomerCoupon> customerCoupons) {
        customerCouponDao.updateBatchById(customerCoupons,100);
    }
}
