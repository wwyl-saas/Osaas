package com.fate.api.merchant;

import com.fate.api.merchant.service.OrderService;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.Order;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-07-30 17:55
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class TestOrderCreate {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    OrderService orderService;

    @Test
    public void testCreateOrder(){
        Merchant merchant= new Merchant().setId(1L);
        CurrentMerchantUtil.addMerchant(merchant);
        Order order=orderService.getById(1171854044328898562L);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
