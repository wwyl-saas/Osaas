package com.fate.api.customer.controller;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.PayDto;
import com.fate.api.customer.query.ConfirmPayQuery;
import com.fate.api.customer.service.CustomerAccountService;
import com.fate.api.customer.service.PayService;
import com.fate.common.model.StandardResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: parent
 * @description: 支付相关接口
 * @author: chenyixin
 * @create: 2019-05-22 23:12
 **/
@Api(value = "API - 支付", description = "小程序支付接口")
@RestController
@RequestMapping("/api/v1/pay")
@Slf4j
public class PayController {
    @Autowired
    PayService payService;

    /**
     * 确认支付
     */
    @ApiOperation(value="确认支付", notes="确认支付后首先后台尝试扣除用户余额，余额扣除成功则直接返回成功提示。当余额不足时，直接请求微信支付预付接口并返回预付结果，" +
            "微信会根据接口返回调起微信支付界面<b>注意：此接口调用需要具有token参数</b>")
    @ApiImplicitParam(name = "query", value = "确认支付参数对象",  dataType = "ConfirmPayQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @PostMapping("/confirm")
    public PayDto confirmOrder(@Validated @RequestBody ConfirmPayQuery query) {
        return payService.confirmOrder(query);
    }

    /**
     * 放弃支付
     */
    @ApiOperation(value="放弃支付", notes="在订单页面点击放弃支付或者退出当前页面")
    @ApiImplicitParam(name = "orderId", value = "订单ID",  dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @PostMapping("/abandon")
    public StandardResponse abandonOrder(@RequestParam Long orderId) {
        payService.abandonOrder(orderId);
        return StandardResponse.success();
    }


    /**
     * <pre>
     * 查询订单微信支付结果
     * </pre>
     */
    @ApiOperation(value="查询订单微信支付结果", notes="主动查询微信支付结果")
    @ApiImplicitParam(name = "payId", value = "订单ID", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @GetMapping(value = "/query")
    public StandardResponse query(@RequestParam Long payId) {
        if (payService.queryPayResult(payId)){
            return StandardResponse.success(true);
        }
        return StandardResponse.success(false);
    }
}
