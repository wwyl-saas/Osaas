package com.fate.api.admin.scheduler.job;

import com.fate.api.admin.service.CouponService;
import com.fate.api.admin.service.MerchantService;
import com.fate.api.admin.service.OrderService;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.Order;
import com.fate.common.enums.OrderStatus;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 定时取消1小时前未支付的订单
 * @author: chenyixin
 * @create: 2019-09-25 15:43
 **/
@Component
@Slf4j
public class CancelOrderJob extends Job{
    @Autowired
    MerchantService merchantService;
    @Autowired
    OrderService orderService;
    @Autowired
    CouponService couponService;


    @Override
    void work() {
        log.info("定时取消1小时前未支付的订单，开始执行......");
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        try {
            List<Merchant> merchants = merchantService.getAllEnable();
            if (CollectionUtils.isNotEmpty(merchants)) {
                for (Merchant merchant : merchants) {
                    //一个小时以前未更新的待支付订单
                    List<Order> orderList=orderService.getByStatusAndCreateTimeBefore(OrderStatus.WAITING_PAY, LocalDateTime.now().minusHours(1),merchant.getId());
                    if (CollectionUtils.isNotEmpty(orderList)){
                        List<Long> couponIds= Lists.newArrayList();
                        List<Order> newList=orderList.parallelStream().map(order -> {
                            if (order.getCouponId()!=null){
                                couponIds.add(order.getCouponId());
                            }
                            Order newOrder=new Order().setId(order.getId()).setVersion(order.getVersion()).setStatus(OrderStatus.CANCELED);
                            return newOrder;
                        }).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(newList)){
                            orderService.updateBatch(newList);
                        }
                        //释放已锁定的优惠券
                        if (CollectionUtils.isNotEmpty(couponIds)){
                            couponService.unlockCoupon(couponIds);
                        }
                    }
                }
            }

            stopWatch.stop();
            log.info("定时取消1小时前未支付的订单，执行完毕，用时{}ms",stopWatch.getTotalTimeMillis());
        }catch (Exception e){
            log.error("取消未支付订单报错：",e);
        }
        //todo 已支付订单超过一个月自动五星好评
    }
}
