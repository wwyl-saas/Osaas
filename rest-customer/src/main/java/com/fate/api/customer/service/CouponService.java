package com.fate.api.customer.service;

import com.fate.common.dao.CouponDao;
import com.fate.common.dao.CustomerCouponDao;
import com.fate.common.entity.Coupon;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.entity.Order;
import com.fate.common.enums.CouponStatus;
import com.fate.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @program: parent
 * @description: 优惠活动及卡项相关
 * @author: chenyixin
 * @create: 2019-05-27 17:29
 **/
@Service
@Slf4j
public class CouponService {
    @Resource
    CustomerCouponDao customerCouponDao;
    @Resource
    CouponDao couponDao;



    /**
     * 根据优惠券ID查询优惠券
     * @param couponId
     * @return
     */
    public CustomerCoupon findCouponById(Long couponId){
        return customerCouponDao.getById(couponId);
    }

    /**
     * 锁定优惠券
     * @param customerCoupon
     */
    public void lockCoupon(CustomerCoupon customerCoupon){
        Assert.notNull(customerCoupon,"锁定的优惠券不能为空");
        customerCoupon.setStatus(CouponStatus.LOCKED);
        customerCouponDao.updateById(customerCoupon);
    }

    /**
     * 解锁优惠券
     * @param couponId
     */
    public void unlockCoupon(Long couponId){
        if (couponId!=null){
            CustomerCoupon customerCoupon=customerCouponDao.getById(couponId);
            Assert.notNull(customerCoupon,"优惠券不存在，couponId="+couponId);
            customerCoupon.setStatus(CouponStatus.NOT_USED);
            customerCouponDao.updateById(customerCoupon);
        }

    }


    /**
     * 消费优惠券
     * @param couponId
     */
    public void consumeCoupon(Long couponId) {
       if (couponId!=null){
           CustomerCoupon customerCoupon=customerCouponDao.getById(couponId);
           if (customerCoupon!=null){
               Assert.isTrue(customerCoupon.getStatus().equals(CouponStatus.LOCKED),"待消费的优惠券为："+customerCoupon.toString());
               CustomerCoupon coupon=new CustomerCoupon();
               coupon.setStatus(CouponStatus.USED);
               coupon.setId(couponId);
               coupon.setUpdateTime(null);
               Assert.isTrue(coupon.updateById(),"数据更新失败");
           }else{
               log.error("优惠券不存在，couponId={}",couponId);
           }
       }
    }

    public  Coupon getCouponById(Long couponId){
        return couponDao.getById(couponId);
    }


    /**
     * 发送优惠券
     * @param coupon
     * @param customerId
     * @param applicationId
     */
    public void dispatchCoupon(Coupon coupon, Long customerId, Long applicationId) {
        Assert.notNull(coupon,"优惠券原型不存在");
        Assert.isTrue(coupon.getEnable(),"优惠券已过期");
        Assert.isTrue(coupon.getIssueNum()==0 || coupon.getIssueNum()>0&&coupon.getResidueNum()>0,"优惠券已经没有库存");

        CustomerCoupon customerCoupon=new CustomerCoupon()
                .setCustomerId(customerId)
                .setCouponId(coupon.getId())
                .setDescription(coupon.getDescription())
                .setDiscount(coupon.getDiscount())
                .setEndTime(coupon.getEndTime())
                .setMerchantAppId(applicationId)
                .setName(coupon.getName())
                .setStartTime(coupon.getStartTime())
                .setStatus(CouponStatus.NOT_USED)
                .setTag(coupon.getTag())
                .setType(coupon.getType());
        Assert.isTrue(customerCoupon.insert(),"插入数据失败");

        coupon.setResidueNum(coupon.getResidueNum()-1);
        coupon.setUpdateTime(null);
        Assert.isTrue( coupon.updateById(),"数据更新成功");
    }
}
