package com.fate.api.customer.controller;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.MyCouponDto;
import com.fate.api.customer.service.CustomerCouponService;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.enums.CouponStatus;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: parent
 * @description: 优惠券及卡券相关接口
 * @author: chenyixin
 * @create: 2019-06-07 12:16
 **/
@Api(value = "API - 卡券及代人支付", description = "需要登录")
@RestController
@RequestMapping("/api/v1/person")
@Slf4j
public class CouponController {
    @Autowired
    private CustomerCouponService customerCouponService;
    /**
     * 获取我的卡券
     * @return
     */
    @Auth(mobile = true)
    @ApiImplicitParam(name = "status", value = "状态 0未使用,1已锁定,2已使用,3已过期", required = true, dataType = "Long", paramType = "query")
    @GetMapping("/coupons")
    public MyCouponDto getMyCoupons(@RequestParam(required = false,defaultValue = "0") Integer status ){
        CouponStatus couponStatus=CouponStatus.getEnum(status).orElse(CouponStatus.NOT_USED);
        return customerCouponService.getMyCoupons(couponStatus);
    }

    /**
     * 卡券兑换
     * @param couponCode (兑换码)
     * @return
     */
    @Auth(mobile = true)
    @GetMapping("/coupons/add")
    public StandardResponse addCoupon(@RequestParam String couponCode){
        customerCouponService.addCouponTOCustomer(couponCode);
        return StandardResponse.success();
    }
}
