package com.fate.api.merchant.controller.mobile;


import com.fate.api.merchant.dto.CustomerDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.service.ChargeService;
import com.fate.api.merchant.service.CustomerService;
import com.fate.common.enums.CustomerSource;
import com.fate.common.enums.MemberLevel;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @description: 会员相关
 * @author: lpx
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - customer", description = "商户会员管理")
@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ChargeService chargeService;


    @ApiOperation(value = "会员详情", notes = "会员管理-会员详情")
    @ApiImplicitParam(name = "userId", value = "员工id", required = true, dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/detail")
    public CustomerDto detail(@RequestParam Long userId) {
        return customerService.detail(userId);
    }


    @ApiOperation(value = "会员列表", notes = "会员管理-会员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", dataType = "Long"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", dataType = "Long"),
            @ApiImplicitParam(name = "query", value = "电话或者名称", dataType = "String"),
            @ApiImplicitParam(name = "memberLevel", value = "会员等级", dataType = "String"),
            @ApiImplicitParam(name = "customerSource", value = "用户来源", dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<CustomerDto> getCustomerList(@RequestParam(required = false, defaultValue = "1") Long pageIndex,
                                                @RequestParam(required = false, defaultValue = "10") Long pageSize,
                                                @RequestParam(required = false) String query,
                                                @RequestParam(required = false) Integer memberLevel,
                                                 @RequestParam(required = false) Integer customerSource) {
        MemberLevel level=null;
        if (memberLevel!=null){
            level=MemberLevel.getEnum(memberLevel).get();
        }
        CustomerSource source=null;
        if (customerSource!=null){
           source=CustomerSource.getEnum(customerSource).get();
        }
        return customerService.getCustomerList(pageIndex, pageSize, query,level,source);
    }


}

