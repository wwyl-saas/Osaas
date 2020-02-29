package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.query.PhoneQuery;
import com.fate.api.merchant.service.CustomerConsumeService;
import com.fate.api.merchant.service.CustomerService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: parent
 * @description: 会员相关
 * @author:
 * @create: 2019-07-18 17:01
 **/
@Api(value = "API - appointment", description = "会员相关")
@RestController("adminCustomerController")
@RequestMapping("/api/admin/customer")
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerConsumeService customerConsumeService;


    @ApiOperation(value="根据手机号精准查询代付人信息", notes="根据手机号精准查询代付人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", dataType = "Long"),
            @ApiImplicitParam(name = "phoneQuery", value = "电话", required = true,dataType = "PhoneQuery")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/withhold/get")
    public PageDto<CustomerDto> getCustomerList(@RequestParam(required = false,defaultValue = "1") Long pageIndex,
                                                @RequestParam(required = false,defaultValue = "10") Long pageSize,
                                                @RequestParam(required = true) PhoneQuery phoneQuery) {
        String mobile=phoneQuery.getPhone();
        return customerService.getCustomerList(pageIndex,pageSize, mobile,null,null);
    }

    @ApiOperation(value="根据手机号查询会员信息", notes="根据手机号查询会员信息admin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", dataType = "Long"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", dataType = "Long"),
            @ApiImplicitParam(name = "mobile", value = "电话", required = false, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<CustomerDto> getCustomerList(@RequestParam(required = false,defaultValue = "1") Long pageIndex,
                                                @RequestParam(required = false,defaultValue = "10") Long pageSize,
                                                @RequestParam(required = false) String mobile) {

        return customerService.getCustomerListBymobile(pageIndex,pageSize, mobile);
    }

    @ApiOperation(value="根据店面查询会员消费记录查询", notes="根据店面查询会员消费记录查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", dataType = "Long"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", dataType = "Long"),
            @ApiImplicitParam(name = "shopIds", value = "商铺ID集合，用逗号隔开", required = false, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/consume/list")
    public PageDto<CustomerConsumeDto> getCustomerConsumeList(@RequestParam(required = false,defaultValue = "1") Long pageIndex,
                                                              @RequestParam(required = false,defaultValue = "10") Long pageSize,
                                                              @RequestParam(required = false) String shopIds) {

        return customerConsumeService.getCustomerConsumeByShopId(pageIndex,pageSize, shopIds);
    }



    @ApiOperation(value="根据商家ID查询会员卡信息", notes="根据商家ID查询会员卡信息")
    @ApiImplicitParam(name = "merchantId", value = "商家ID从界面选择列表得到", required = false, dataType = "String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/membercard/list")
    public List<MemberCardDto> getCustomerConsumeList(@RequestParam(required = false) String merchantId) {
        return customerConsumeService.getMemberCard(merchantId);
    }

    @ApiOperation(value="根据商家ID和会员卡ID查询会员福利信息", notes="根据商家ID和会员卡ID查询会员福利信息")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "merchantId", value = "商家ID从界面选择列表得到", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "memberId", value = "会员ID从界面选择列表得到", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/memberWelFare/list")
    public List<MemberWelfareDto> getWelFare(@RequestParam(required = true) Long merchantId,
                                             @RequestParam(required = true) Long memberId) {
        return customerConsumeService.getWelFare(merchantId,memberId);
    }



    @ApiOperation(value="根据商家ID和会员卡ID查询会员门槛值信息", notes="根据商家ID和会员卡ID查询会员门槛值信息")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "merchantId", value = "商家ID从界面选择列表得到", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "memberId", value = "会员ID从界面选择列表得到", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/memberThreshold/list")
    public List<MemberThresholdDto> getMemberThreshold(@RequestParam(required = true) Long merchantId,
                                                          @RequestParam(required = true) Long memberId
    ) {
        return customerConsumeService.getMemberThreshold(merchantId,memberId);
    }












}
