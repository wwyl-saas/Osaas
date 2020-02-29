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
 * @description: 订单通知
 * @author: chenyixin
 * @create: 2019-09-13 11:02
 **/
@RestController
@RequestMapping("/api/server/order")
@Slf4j
public class OrderNoticeController {
    @Autowired
    OrderService orderService;

    @PostMapping("/send/cancel")
    public StandardResponse sendOrderCancelToCustomer(@RequestParam Long orderId) throws WxPayException {
        orderService.sendOrderCancelToCustomer(orderId);
        return StandardResponse.success();
    }


    @PostMapping("/send/settle")
    public StandardResponse sendOrderSettleToCustomer(@RequestParam Long orderId){
        orderService.sendOrderSettleToCustomer(orderId);
        return StandardResponse.success();
    }
}
