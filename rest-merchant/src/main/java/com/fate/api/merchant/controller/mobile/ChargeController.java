package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.dto.ChargeDto;
import com.fate.api.merchant.dto.CustomerAccountDto;
import com.fate.api.merchant.service.ChargeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 会员充值
 * @author: chenyixin
 * @create: 2019-08-06 00:28
 **/
@Api(value = "API - customer", description = "商户会员管理")
@RestController
@RequestMapping("/api/recharge")
@Slf4j
public class ChargeController {
    @Autowired
    ChargeService chargeService;

    /**
     * 线下充值
     * @return
     */
    @PostMapping("/offline")
    public CustomerAccountDto recharge(@RequestParam Long shopId,
                                       @RequestParam Long customerId,
                                       @RequestParam BigDecimal amount,
                                       @RequestParam BigDecimal giftAmount,
                                       @RequestParam(required = false) Long userId) {
        return chargeService.chargeOffline(shopId,customerId,amount,giftAmount,userId);
    }

    /**
     * 充值详情
     * @return
     */
    @GetMapping("/detail")
    public ChargeDto getDetail(@RequestParam Long chargeId) {
        return chargeService.getDetail(chargeId);
    }
}
