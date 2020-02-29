package com.fate.api.customer.controller;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.PayDto;
import com.fate.api.customer.service.ChargeService;
import com.fate.common.model.StandardResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 充值相关
 * @author: chenyixin
 * @create: 2019-06-14 15:52
 **/
@Api(value = "API - 充值相关", description = "充值接口")
@RestController
@RequestMapping("/api/v1/charge")
@Slf4j
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    /**
     * 会员充值
     */
    @ApiOperation(value="会员充值接口", notes="个人中心页-会员福利-充值（需要登录）")
    @ApiImplicitParam(name = "chargeAmount", value = "充值金额,元为单位的整数", required = true, dataType = "int", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @PostMapping("/member/charge")
    public PayDto memberCharge(@RequestParam BigDecimal chargeAmount) {
        return chargeService.memberCharge(chargeAmount);
    }

    /**
     * <pre>
     * 查询充值记录微信支付结果
     * </pre>
     */
    @ApiOperation(value="查询充值记录微信支付结果", notes="主动查询微信支付结果")
    @ApiImplicitParam(name = "payId", value = "充值记录ID", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @GetMapping(value = "/query")
    public StandardResponse query(@RequestParam Long payId) throws WxPayException {
        if (chargeService.queryChargePay(payId)){
            return StandardResponse.success(true);
        }
        return StandardResponse.success(false);
    }

}
