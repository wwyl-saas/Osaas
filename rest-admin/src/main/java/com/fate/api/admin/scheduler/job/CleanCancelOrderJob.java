package com.fate.api.admin.scheduler.job;

import com.fate.api.admin.service.MerchantService;
import com.fate.api.admin.service.OrderService;
import com.fate.common.dao.OrderDao;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.Order;
import com.fate.common.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时删除(两周前)取消订单
 */
@Component
@Slf4j
public class CleanCancelOrderJob extends Job{
    @Autowired
    MerchantService merchantService;
    @Autowired
    OrderService orderService;

    @Override
    void work() {
        log.info("定时删除(两周前)取消订单，开始执行......");
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        try {
            List<Merchant> merchants = merchantService.getAllEnable();
            if (CollectionUtils.isNotEmpty(merchants)) {
                for (Merchant merchant : merchants) {
                    //一个小时以前未更新的待支付订单
                    List<Order> orderList=orderService.getByStatusAndCreateTimeBefore(OrderStatus.CANCELED, LocalDateTime.now().minusWeeks(2),merchant.getId());
                    if (CollectionUtils.isNotEmpty(orderList)){
                        List<Long> idList=orderList.parallelStream().map(order -> order.getId()).collect(Collectors.toList());
                        orderService.deleteBatch(idList);
                    }
                }
            }

            stopWatch.stop();
            log.info("定时删除(两周前)取消订单，执行完毕，用时{}ms",stopWatch.getTotalTimeMillis());
        }catch (Exception e){
            log.error("定时删除(两周前)取消订单报错：",e);
        }
    }
}
