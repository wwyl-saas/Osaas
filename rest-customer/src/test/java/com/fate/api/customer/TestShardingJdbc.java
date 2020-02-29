package com.fate.api.customer;

import com.fate.common.dao.OrderDao;
import com.fate.common.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: parent
 * @description: 测试分库分表
 * @author: chenyixin
 * @create: 2019-04-30 11:19
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class TestShardingJdbc {
    @Resource
    OrderDao orderDao;
//
//    @Test
//    public void testInsert(){
//        Order order = new Order();
//        order.setOrderNo("A123456789");
//        order.setUserId(1);
//        order.setOrderDate(LocalDateTime.now());
//        order.setTenantId(2);
//        orderDao.save(order);
//    }
//
//
//    @Test
    public void testSelect(){
        Order order=orderDao.getById("1139056850303799297");
        log.info(order.toString());
    }

    @Test
    public void test(){

    }
}
