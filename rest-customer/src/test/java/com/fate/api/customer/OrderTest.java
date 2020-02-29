package com.fate.api.customer;

import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.Order;
import com.fate.common.entity.OrderDesc;
import com.fate.common.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @program: parent
 * @description: 订单测试
 * @author: chenyixin
 * @create: 2019-06-13 14:10
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class OrderTest {
//    @Test
    public void test1() {
//        CurrentInfoUtil.addMerchantApplication(new MerchantApplication().setMerchantId(1L));
        List<Integer> list = Arrays.asList(0, 1, 2, 3);

        Order order=new Order();
        order.setStatus(OrderStatus.WAITING_PAY).setDiscountAmount(BigDecimal.valueOf(5))
                .setPayAmount(BigDecimal.valueOf(138))
                .setOrderSumAmount(BigDecimal.valueOf(143))
                .setCouponAmount(BigDecimal.valueOf(5))
                .setCouponId(1L)
                .setCustomerId(CurrentCustomerUtil.getCustomer().getId())
                .setMerchantAppId(CurrentApplicationUtil.getMerchantApplication().getId())
                .setMerchantUserId(1138802565754208258L)
                .setOrderNo("")
                .setShopId(1138394767358758914L)
                .setTotalNum(2)
                .insert();

        OrderDesc orderDesc=new OrderDesc();
        orderDesc.setBriefPicUrl("1.png")
        .setCounterPrice(BigDecimal.TEN).setGoodsBrief("商品简介").setGoodsNum(1).setMarketPrice(BigDecimal.ONE).setGoodsId(1138394767014825986L).setName("高级减肥").setOrderId(order.getId()).insert();

        OrderDesc orderDesc1=new OrderDesc();
        orderDesc1.setBriefPicUrl("2.jpg")
                .setCounterPrice(BigDecimal.TEN).setGoodsBrief("商品简介").setGoodsNum(1).setMarketPrice(BigDecimal.ONE).setGoodsId(1138394768357003266L).setName("高级剪发").setOrderId(order.getId()).insert();

    }
    @Test
    public void test(){

    }
}
