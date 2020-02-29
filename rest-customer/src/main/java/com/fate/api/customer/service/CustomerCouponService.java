package com.fate.api.customer.service;

import com.fate.api.customer.dto.CardDto;
import com.fate.api.customer.dto.CouponDto;
import com.fate.api.customer.dto.MyCouponDto;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.CouponDao;
import com.fate.common.dao.CustomerCardDao;
import com.fate.common.dao.CustomerCouponDao;
import com.fate.common.entity.*;
import com.fate.common.enums.CouponStatus;
import com.fate.common.enums.CouponType;
import com.fate.common.util.BeanUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: rest-customer
 * @description:用户卡券操作
 * @author: xudongdong
 * @create: 2019-05-29 23:15
 **/

@Service
@Slf4j
public class CustomerCouponService {

    @Resource
    CustomerCouponDao customerCouponDao;
    @Resource
    CustomerCardDao customerCardDao;
    @Resource
    CouponDao couponDao;

    /**
     * 获取当前用户的卡券
     * @return
     * @param status
     */
    public MyCouponDto getMyCoupons(CouponStatus status){
        MyCouponDto result=new MyCouponDto();
        Customer customer= CurrentCustomerUtil.getCustomer();
        List<CouponStatus> statuses=Lists.newArrayList(status);
        //已锁定直接显示
        if (status.equals(CouponStatus.NOT_USED)){
            statuses.add(CouponStatus.LOCKED);
        }
        List<CustomerCoupon> customerCoupons=customerCouponDao.selectByStatusAndCustomerId(customer.getId(),statuses);
        if (CollectionUtils.isNotEmpty(customerCoupons)){
            List<CouponDto> couponDtos=customerCoupons.stream().map(customerCoupon->BeanUtil.mapper(customerCoupon, CouponDto.class)).collect(Collectors.toList());
            result.setCoupons(couponDtos);
        }
        List<CustomerCard> customerCards=customerCardDao.findByCustomerId(customer.getId());
        if (CollectionUtils.isNotEmpty(customerCards)){
            List<CardDto> couponDtos=customerCards.stream().map(customerCard->{
                CardDto cardDto= BeanUtil.mapper(customerCard, CardDto.class);
                cardDto.setStatusCode(customerCard.getStatus().getCode());
                return cardDto;
            }).collect(Collectors.toList());
            result.setCards(couponDtos);
        }
        return result;
    }


    /**
     * 兑换优惠券
     * @param couponCode
     * @return
     */
    public void addCouponTOCustomer(String couponCode){
        Map<String,Long> temMap=new HashMap<>();
        temMap.put("111111",1171122861681369090L);
        temMap.put("222222",1171122908535939074L);
        temMap.put("333333",1111222918846390274L);
        Long couponId=temMap.get(couponCode);
        Assert.notNull(couponId,"请输入正确的兑换码");

        Coupon coupon=couponDao.getById(couponId);
        Assert.notNull(coupon,"优惠券原型不存在");
        Assert.isTrue(coupon.getEnable(),"您的兑换码已过期");
        Assert.isTrue(coupon.getIssueNum()==0 || coupon.getIssueNum()>0&&coupon.getResidueNum()>0,"你兑换的优惠券已经没有库存");

        CustomerCoupon customerCoupon=new CustomerCoupon()
                .setCustomerId(CurrentCustomerUtil.getCustomer().getId())
                .setCouponId(coupon.getId())
                .setDescription(coupon.getDescription())
                .setDiscount(coupon.getDiscount())
                .setEndTime(coupon.getEndTime())
                .setMerchantAppId(CurrentApplicationUtil.getMerchantApplication().getId())
                .setName(coupon.getName())
                .setStartTime(coupon.getStartTime())
                .setStatus(CouponStatus.NOT_USED)
                .setTag(coupon.getTag())
                .setType(coupon.getType());
        Assert.isTrue(customerCoupon.insert(),"数据插入成功");

        coupon.setResidueNum(coupon.getResidueNum()-1);
        coupon.setUpdateTime(null);
        Assert.isTrue( coupon.updateById(),"数据更新成功");
    }


}

