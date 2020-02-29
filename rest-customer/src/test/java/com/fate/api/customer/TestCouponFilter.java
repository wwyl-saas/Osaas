package com.fate.api.customer;

import com.fate.api.customer.dto.OrderCouponDto;
import com.fate.api.customer.service.CouponFilterService;
import com.fate.common.dao.OrderDao;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.Order;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    OrderDao orderDao;
    @Autowired
    CouponFilterService couponFilterService;

    @Test
    public void testCreateOrder(){
        Merchant merchant= new Merchant().setId(1L);
        CurrentMerchantUtil.addMerchant(merchant);
        Order order=orderDao.getById(1181384638086520833L);
        List<OrderCouponDto> customerCoupon=couponFilterService.matchOrderCoupons(order);
        log.info("result:{}",customerCoupon.toString());
    }

}
