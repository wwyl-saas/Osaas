package com.fate.api.merchant.service;

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
import com.fate.common.rule.RuleFilterFactory;
import com.fate.common.rule.filter.RuleFilter;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
     * 匹配订单的最优优惠券
     *
     * @param order
     * @return
     */
    public CustomerCoupon matchOrderCoupon(final Order order) {
        List<CustomerCoupon> customerCoupons = customerCouponDao.selectByStatusAndTypeAndCustomerId(order.getCustomerId(), Lists.list(CouponStatus.NOT_USED), Lists.list(CouponType.CASH_COUPON, CouponType.DISCOUNT_COUPON));
        if (CollectionUtils.isNotEmpty(customerCoupons)) {
            RuleContext ruleContext = new RuleContext();
            ruleContext.addFact("orderAmount", order.getOrderSumAmount());
            ruleContext.addFact("totalNum", order.getTotalNum());
            List<OrderDesc> orderDescs = orderDescDao.getByOrderId(order.getId());
            Assert.notEmpty(orderDescs, "订单详情为空");
            String goodIds = orderDescs.stream().map(orderDesc -> orderDesc.getGoodsId().toString()).collect(Collectors.joining(","));
            ruleContext.addFact("goodsIds", goodIds);

            Optional<CustomerCoupon> match = customerCoupons.stream().filter(customerCoupon -> {
                List<RuleFilter> ruleFilters = getFilterList(customerCoupon);
                if (CollectionUtils.isNotEmpty(ruleFilters)) {
                    ruleFilters.stream().forEach(ruleFilter -> ruleFilter.match(ruleContext));
                }
                boolean result = ruleContext.getResult();
                ruleContext.clear();
                return result;
            }).map(customerCoupon -> {
                if (customerCoupon.getType().equals(CouponType.CASH_COUPON)) {
                    customerCoupon.setAmount(BigDecimal.valueOf(customerCoupon.getDiscount() / 100));//单位转换为元
                } else if (customerCoupon.getType().equals(CouponType.DISCOUNT_COUPON)) {
                    customerCoupon.setAmount(order.getOrderSumAmount().multiply(BigDecimal.valueOf(customerCoupon.getDiscount() / 100)));//此处100为转换百分比
                }
                return customerCoupon;
            }).sorted(Comparator.comparing(CustomerCoupon::getAmount).reversed()).findFirst();
            return match.orElse(null);
        }
        return null;
    }

    /**
     * 获取优惠券过滤器链
     *
     * @param customerCoupon
     * @return
     */
    private List<RuleFilter> getFilterList(CustomerCoupon customerCoupon) {
        List<RuleFilter> result = Lists.emptyList();
        List<CouponEffectRule> effectRules = couponEffectRuleDao.getByCouponId(customerCoupon.getCouponId());
        if (CollectionUtils.isNotEmpty(effectRules)) {
            result = effectRules.stream().map(effectRule -> RuleFilterFactory.createFilter(effectRule)).collect(Collectors.toList());
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
