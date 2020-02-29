package com.fate.api.customer.controller;

import com.fate.api.customer.service.OrderService;
import com.fate.common.model.StandardResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: parent
 * @description: 预约通知
 * @author: chenyixin
 * @create: 2019-09-13 10:38
 **/
@RestController
@RequestMapping("/api/server")
@Slf4j
public class FeignController {
    @Autowired
    OrderService orderService;

    /**
     * 订单创建完成推送客户端
     * @param orderId
     * @return
     */
    @PostMapping("/order/create")
    public StandardResponse sendOrderCreateToCustomer(@RequestParam Long orderId){
        orderService.sendOrderCreateToCustomer(orderId);
        return StandardResponse.success();
    }



    /**
     * 订单取消推送客户端
     * @param orderId
     * @return
     */
    @PostMapping("/order/cancel")
    public StandardResponse sendOrderCancelToCustomer(@RequestParam Long orderId) throws WxPayException {
        orderService.sendOrderCancelToCustomer(orderId);
        return StandardResponse.success();
    }


}
