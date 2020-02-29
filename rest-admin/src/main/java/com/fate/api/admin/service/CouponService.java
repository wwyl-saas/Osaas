package com.fate.api.admin.service;

import com.fate.common.dao.CouponDao;
import com.fate.common.dao.CustomerCouponDao;
import com.fate.common.entity.Coupon;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.enums.CouponStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 优惠券
 * @author: chenyixin
 * @create: 2019-09-19 21:42
 **/
@Service
@Slf4j
public class CouponService {
    @Resource
    CouponDao couponDao;
    @Resource
    CustomerCouponDao customerCouponDao;


    public Coupon getById(long couponId) {
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


    /**
     * 解锁优惠券
     * @param couponIds
     */
    public void unlockCoupon(List<Long> couponIds){
        if (!CollectionUtils.isEmpty(couponIds)){
            List<CustomerCoupon> customerCoupons= (List<CustomerCoupon>) customerCouponDao.listByIds(couponIds);
            if (!CollectionUtils.isEmpty(customerCoupons)){
                List<CustomerCoupon> updateList=customerCoupons.stream().filter(customerCoupon -> customerCoupon!=null).map(customerCoupon -> {
                    CustomerCoupon coupon=new CustomerCoupon();
                    coupon.setId(customerCoupon.getId());
                    coupon.setStatus(CouponStatus.NOT_USED);
                    return coupon;
                }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(updateList)){
                    customerCouponDao.updateBatchById(updateList,100);
                }
            }
        }

    }

    /**
     *
     * @return
     */
    public List<Coupon> getEnableAll() {
        return couponDao.getEnableAll();
    }

    public void updateBatch(List<Coupon> expireList) {
        couponDao.updateBatchById(expireList,100);
    }
}
