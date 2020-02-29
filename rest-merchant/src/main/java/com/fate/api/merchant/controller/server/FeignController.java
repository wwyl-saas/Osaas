package com.fate.api.merchant.controller.server;

import com.fate.api.merchant.annotation.AddMerchant;
import com.fate.api.merchant.annotation.AuthIgnore;
import com.fate.api.merchant.service.OrderService;
import com.fate.common.model.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: parent
 * @description: 订单
 * @author: chenyixin
 * @create: 2019-09-13 01:31
 **/
@RestController
@RequestMapping("/api/server")
@Slf4j
public class FeignController {
    @Autowired
    OrderService orderService;
    /**
     * 给商户用户发送订单取消
     * @return
     */
    @AddMerchant
    @PostMapping("/order/abandon")
    @AuthIgnore
    public StandardResponse sendOrderAbandonToUser(@RequestParam Long orderId){
        orderService.sendOrderAbandonToUser(orderId);
        return StandardResponse.success();
    }

    /**
     * 给商户用户发送订单结算结果
     * @return
     */
    @AddMerchant
    @PostMapping("/order/settle")
    @AuthIgnore
    public StandardResponse sendOrderSettleToUser(@RequestParam Long orderId){
        orderService.sendOrderSettleToUser(orderId);
        return StandardResponse.success();
    }


    /**
     * 给商户用户发送订单结算结果
     * @return
     */
    @AddMerchant
    @PostMapping("/order/change")
    @AuthIgnore
    public StandardResponse sendOrderChangeToUser(@RequestParam Long orderId){
        orderService.sendOrderChangeToUser(orderId);
        return StandardResponse.success();
    }
}
