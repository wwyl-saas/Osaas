package com.fate.api.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: parent
 * @description: 测试分库分表
 * @author: chenyixin
 * @create: 2019-04-30 11:19
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class TestShardingJdbc {
//    @Autowired
//    OrderDao orderDao;
//    @Autowired
//    OrderItemDao orderItemDao;
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
//    public void testSelect(){
//        List<Order> orderList=orderDao.findByOrderNo("A123456789");
//        orderList.stream().forEach(order -> log.info(order.toString()));
//    }
}
