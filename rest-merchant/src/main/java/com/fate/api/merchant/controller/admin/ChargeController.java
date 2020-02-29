package com.fate.api.merchant.controller.admin;

import com.fate.api.merchant.dto.ChargeDto;
import com.fate.api.merchant.dto.CustomerAccountDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.service.ChargeService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @program: parent
 * @description: admin 会员充值
 * @author:
 * @create: 2019-08-06 00:28
 **/
@Api(value = "API - customer", description = "商户会员充值管理")
@RestController("adminChargeController")
@RequestMapping("/api/admin/recharge")
@Slf4j
public class ChargeController {
    @Autowired
    ChargeService chargeService;


    /**
     * 充值金额
     */
    @ApiOperation(value="充值金额", notes="充值金额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺id",required = true,
                    dataType = "Long"),
            @ApiImplicitParam(name = "customerId", value = "会员id",required = true,
                    dataType = "Long"),
            @ApiImplicitParam(name = "amount", value = "充值金额",required = true,
                    dataType = "BigDecimal"),
            @ApiImplicitParam(name = "giftAmount", value = "赠送金额",required = true,
                    dataType = "BigDecimal"),
            @ApiImplicitParam(name = "userId", value = "商户id",
                    dataType = "Long" )

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/offline")
    public CustomerAccountDto recharge(@RequestParam Long shopId,
                                       @RequestParam Long customerId,
                                       @RequestParam BigDecimal amount,
                                       @RequestParam BigDecimal giftAmount,
                                       @RequestParam(required = false) Long userId) {
        return chargeService.chargeOffline(shopId,customerId,amount,giftAmount,userId);
    }

    /**
     * 查询当前商户某些门店的充值记录，门店以逗号分隔开
     *
     */
    @ApiOperation(value="当前商户的某些门店的充值情况", notes="当前商户的某些门店的充值情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopIds", value = "店铺id集合",required = false, dataType = "Long")

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<ChargeDto> getChargeList(@RequestParam(required = false) String shopIds, @RequestParam(required = false,defaultValue = "1") Integer pageIndex, @RequestParam(required = false,defaultValue = "10") Integer pageSize
                                       ) {
        return chargeService.getChargeList(shopIds,pageIndex,pageSize);
    }












}
