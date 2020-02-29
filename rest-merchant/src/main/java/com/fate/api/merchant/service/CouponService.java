package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.dto.CouponDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.CouponQuery;
import com.fate.api.merchant.query.GoodsQuery;
import com.fate.common.dao.CouponDao;
import com.fate.common.dao.CustomerCouponDao;
import com.fate.common.entity.Coupon;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.entity.Order;
import com.fate.common.enums.CouponStatus;
import com.fate.common.enums.CouponType;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 优惠券
 * @author: chenyixin
 * @create: 2019-07-22 23:54
 **/
@Service
@Slf4j
public class CouponService {
    @Resource
    CustomerCouponDao customerCouponDao;
    @Resource
    CouponDao couponDao;

    /**
     * 消费优惠券
     * @param couponId
     */
    public void consumeCoupon(Long couponId) {
        if (couponId!=null){
            CustomerCoupon customerCoupon=customerCouponDao.getById(couponId);
            if (customerCoupon!=null){
                if (customerCoupon.getStatus().equals(CouponStatus.LOCKED)){
                    CustomerCoupon coupon=new CustomerCoupon();
                    coupon.setStatus(CouponStatus.USED);
                    coupon.setId(couponId);
                    boolean flag=coupon.updateById();
                    Assert.isTrue(flag,"数据更新失败");
                }else{
                    log.error("待消费的优惠券为：{}",customerCoupon.toString());
                    throw new BaseException(ResponseInfo.COUPON_STATUS_ERROR);
                }
            }else{
                log.error("优惠券不存在，couponId={}",couponId);
            }
        }
    }


    /**
     * 新建优惠券
     * @param couponQuery
     */
    public void saveCoupon(CouponQuery couponQuery) {
        Coupon coupon=new Coupon();
        coupon.setDescription(couponQuery.getDescription());
        if (couponQuery.getType().equals(CouponType.CASH_COUPON)){
            coupon.setDiscount(couponQuery.getDiscountAmount().multiply(BigDecimal.valueOf(100L)).intValue());//分为单位
        }else if (couponQuery.getType().equals(CouponType.DISCOUNT_COUPON)){
            coupon.setDiscount(couponQuery.getDiscountRatio());
        }else if (couponQuery.getType().equals(CouponType.CHARGE_COUPON)){
            coupon.setDiscount(couponQuery.getDiscountAmount().multiply(BigDecimal.valueOf(100L)).intValue());
        }
        coupon.setName(couponQuery.getName());
        coupon.setResidueNum(couponQuery.getResidueNum());
        coupon.setTag(couponQuery.getTag());
        coupon.setType(couponQuery.getType());
        coupon.setIssueNum(couponQuery.getIssueNum());
        coupon.setEnable(couponQuery.getEnable());
        Assert.isTrue(coupon.insert(),"插入数据失败");

    }

    /**
     * 修改优惠券
     * @param couponQuery
     */
    public void updateCoupon(CouponQuery couponQuery) {
        Assert.notNull(couponQuery.getId(),"id不能为空！");
        Coupon coupon=couponDao.getById(couponQuery.getId());
        Assert.notNull(coupon,"优惠券不存在！");
        coupon.setDescription(couponQuery.getDescription());
        if (couponQuery.getType().equals(CouponType.CASH_COUPON)){
            coupon.setDiscount(couponQuery.getDiscountAmount().multiply(BigDecimal.valueOf(100L)).intValue());//分为单位
        }else if (couponQuery.getType().equals(CouponType.DISCOUNT_COUPON)){
            coupon.setDiscount(couponQuery.getDiscountRatio());
        }else if (couponQuery.getType().equals(CouponType.CHARGE_COUPON)){
            coupon.setDiscount(couponQuery.getDiscountAmount().multiply(BigDecimal.valueOf(100L)).intValue());
        }
        coupon.setName(couponQuery.getName());
        coupon.setResidueNum(couponQuery.getResidueNum());
        coupon.setTag(couponQuery.getTag());
        coupon.setType(couponQuery.getType());
        coupon.setIssueNum(couponQuery.getIssueNum());
        coupon.setEnable(couponQuery.getEnable());

        Assert.isTrue(coupon.updateById(),"更新数据失败");


    }

    /**
     * 删除优惠券
     * @param id
     */
    public void deleteCoupon(Long id) {
        Assert.notNull(id,"id不能为空！");
        Coupon coupon=couponDao.getById(id);
        Assert.notNull(coupon,"优惠券不存在！");
        Assert.isTrue(coupon.deleteById(),"删除失败！"); ;
    }

    /**
     * 查询商户下的所有优惠券
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public PageDto<CouponDto> queryCouponByName(Integer pageIndex, Integer pageSize, String name) {
        IPage<Coupon> page=couponDao.getCouponByNamePage(pageIndex,pageSize,name);
        List<CouponDto> result= Lists.emptyList();
        if (page!=null && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<Coupon> coupons = page.getRecords();
            result = coupons.stream().map(coupon -> {
                CouponDto couponDto = BeanUtil.mapper(coupon, CouponDto.class);
                return couponDto;
            }).collect(Collectors.toList());
        }
        return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);

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
