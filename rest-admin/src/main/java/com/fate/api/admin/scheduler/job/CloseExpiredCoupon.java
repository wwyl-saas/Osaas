package com.fate.api.admin.scheduler.job;

import com.fate.api.admin.service.CouponService;
import com.fate.api.admin.service.CustomerCouponService;
import com.fate.common.entity.Coupon;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.enums.CouponStatus;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 关闭过期优惠券无库存优惠券
 * @author: chenyixin
 * @create: 2019-09-25 15:47
 **/
@Component
@Slf4j
public class CloseExpiredCoupon extends Job {
    @Autowired
    CustomerCouponService customerCouponService;
    @Autowired
    CouponService couponService;


    @Override
    void work() {
        List<Coupon> coupons = couponService.getEnableAll();
        List<Long> expiredCouponIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(coupons)) {
            //关闭无库存
            List<Coupon> expireList1 = coupons.parallelStream().filter(coupon -> coupon.getResidueNum() <= 0).map(coupon -> {
                coupon.setEnable(false);
                return coupon;
            }).collect(Collectors.toList());

            //关闭过期
            List<Coupon> expireList2 = coupons.parallelStream().filter(coupon -> DateUtil.ifExpired(coupon.getEndTime())).map(coupon -> {
                expiredCouponIds.add(coupon.getId());
                coupon.setEnable(false);
                return coupon;
            }).collect(Collectors.toList());


            List<Coupon> list = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(expireList1)) {
                list.addAll(expireList1);
            }
            if (CollectionUtils.isNotEmpty(expireList2)) {
                list.addAll(expireList2);
            }
            if (CollectionUtils.isNotEmpty(list)){
                couponService.updateBatch(list);
            }
            //todo 清除兑换码
            //todo 优惠券下发需要判断是否符合日期
        }

        //此处要求下发后不能更改原始优惠券的过期时间
        if (CollectionUtils.isNotEmpty(expiredCouponIds)) {
            //查找未使用的优惠券
            List<CustomerCoupon> customerCoupons=customerCouponService.getByCouponIds(expiredCouponIds);
            if (CollectionUtils.isNotEmpty(customerCoupons)){
                customerCoupons.parallelStream().forEach(customerCoupon -> customerCoupon.setStatus(CouponStatus.EXPIRED));
                customerCouponService.updateBatch(customerCoupons);
            }
        }

        //todo 已使用优惠券从用户表移动到历史表
    }
}
