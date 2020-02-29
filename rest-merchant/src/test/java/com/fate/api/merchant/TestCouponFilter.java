package com.fate.api.merchant;

import com.fate.api.merchant.service.CouponFilterService;
import com.fate.api.merchant.service.OrderService;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.Order;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-24 23:32
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class TestCouponFilter {
    @Autowired
    OrderService orderService;
    @Autowired
    CouponFilterService couponFilterService;

    @Test
    public void testCreateOrder(){
        Merchant merchant= new Merchant().setId(1L);
        CurrentMerchantUtil.addMerchant(merchant);
        Order order=orderService.getById(1173260019764948993L);
        CustomerCoupon customerCoupon=couponFilterService.matchOrderCoupon(order);
        log.info("result:",customerCoupon.toString());
    }

}
