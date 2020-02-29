package com.fate.api.customer.service;

import com.fate.api.customer.dto.OrderCouponDto;
import com.fate.common.dao.CouponEffectRuleDao;
import com.fate.common.dao.CustomerCouponDao;
import com.fate.common.dao.OrderDescDao;
import com.fate.common.entity.CouponEffectRule;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.entity.Order;
import com.fate.common.entity.OrderDesc;
import com.fate.common.enums.CouponStatus;
import com.fate.common.enums.CouponType;
import com.fate.common.rule.RuleContext;
import com.fate.common.rule.filter.RuleFilter;
import com.fate.common.rule.RuleFilterFactory;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 优惠券匹配
 * @author: chenyixin
 * @create: 2019-09-18 17:45
 **/
@Service
@Slf4j
public class CouponFilterService {
    @Resource
    CustomerCouponDao customerCouponDao;
    @Resource
    CouponEffectRuleDao couponEffectRuleDao;
    @Resource
    OrderDescDao orderDescDao;


    /**
     * 匹配订单的优惠券列表
     *
     * @param order
     * @return
     */
    public List<OrderCouponDto> matchOrderCoupons(Order order) {
        List<OrderCouponDto> result=Lists.newArrayList();
        List<OrderCouponDto> notMatch=Lists.newArrayList();
        List<CustomerCoupon> customerCoupons = customerCouponDao.selectByStatusAndTypeAndCustomerId(order.getCustomerId(), Lists.list(CouponStatus.NOT_USED,CouponStatus.LOCKED),Lists.list(CouponType.CASH_COUPON,CouponType.DISCOUNT_COUPON));
        if (CollectionUtils.isNotEmpty(customerCoupons)) {
            //只要未使用和自己订单锁定的优惠券
            if (order.getCouponId()!=null){
                customerCoupons=customerCoupons.stream().filter(customerCoupon -> customerCoupon.getStatus().equals(CouponStatus.NOT_USED) || customerCoupon.getId().equals(order.getCouponId())).collect(Collectors.toList());
            }else{
                customerCoupons=customerCoupons.stream().filter(customerCoupon -> customerCoupon.getStatus().equals(CouponStatus.NOT_USED)).collect(Collectors.toList());
            }

            RuleContext ruleContext=new RuleContext();
            ruleContext.addFact("orderAmount",order.getOrderSumAmount());
            ruleContext.addFact("totalNum",order.getTotalNum());
            List<OrderDesc> orderDescs=orderDescDao.getByOrderId(order.getId());
            Assert.notEmpty(orderDescs,"订单详情为空");
            String goodIds=orderDescs.stream().map(orderDesc -> orderDesc.getGoodsId().toString()).collect(Collectors.joining(","));
            ruleContext.addFact("goodsIds",goodIds);
            for(CustomerCoupon customerCoupon:customerCoupons){
                OrderCouponDto orderCouponDto= BeanUtil.mapper(customerCoupon,OrderCouponDto.class);
                orderCouponDto.setId(customerCoupon.getId().toString());
                orderCouponDto.setMatch(true);
                List<RuleFilter> ruleFilters=getFilterList(customerCoupon);
                if (CollectionUtils.isNotEmpty(ruleFilters)){
                    ruleFilters.stream().forEach(ruleFilter -> ruleFilter.match(ruleContext));
                    orderCouponDto.setMatch(ruleContext.getResult());
                    orderCouponDto.setReasons(ruleContext.getReasons());
                }
                //区分存放
                if (orderCouponDto.isMatch()){
                    result.add(orderCouponDto);
                }else{
                    notMatch.add(orderCouponDto);
                }
                ruleContext.clear();
            }
        }
        result.addAll(notMatch);
        return result;
    }

    /**
     * 获取优惠券过滤器链
     *
     * @param customerCoupon
     * @return
     */
    private List<RuleFilter> getFilterList(CustomerCoupon customerCoupon) {
        List<RuleFilter> result= Lists.emptyList();
        List<CouponEffectRule> effectRules = couponEffectRuleDao.getByCouponId(customerCoupon.getCouponId());
        if (CollectionUtils.isNotEmpty(effectRules)) {
            result=effectRules.stream().map(effectRule -> RuleFilterFactory.createFilter(effectRule)).collect(Collectors.toList());
        }
        return result;
    }


    /**
     * 根据ticketCode下发优惠券
     *
     * @param customerId
     * @param ticketCode
     * @return
     */
    public CustomerCoupon issueCoupon(Long customerId, String ticketCode) {
        return null;
    }


    /**
     * 根据商户couponId下发优惠券
     *
     * @param customerId
     * @param couponId
     * @return
     */
    public CustomerCoupon issueCoupon(Long customerId, Long couponId) {
        return null;
    }


}
