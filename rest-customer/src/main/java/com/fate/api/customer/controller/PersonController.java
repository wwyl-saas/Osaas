package com.fate.api.customer.controller;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.*;
import com.fate.api.customer.service.*;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.enums.BillType;
import com.fate.common.util.BeanUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @program: rest-customer
 * @description: 个人中心相关接口
 * @author: xudongdong
 * @create: 2019-05-26 11:16
 **/
@Api(value = "API - 个人中心", description = "小程序个人中心接口")
@RestController
@RequestMapping("/api/v1/person")
@Slf4j
public class PersonController{

    @Autowired
    private CustomerService customerService;
    @Autowired
    private MemberService memberService;
    @Autowired
    CustomerConsumeService customerConsumeService;

    /**
     * 个人中心页首页
     */
    @ApiOperation(value="个人中心页首页", notes="需要登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/index")
    @Auth
    public CustomerDto getPersonIndex(){
        return customerService.getPersonIndex();
    }


    /**
     * 获取会员福利页数据
     */
    @ApiOperation(value="获取会员福利信息", notes="个人中心页-会员福利（需要登录）")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @GetMapping("/welfare")
    public MemberWelfareDto getMemberWelfare(){
        return memberService.getMemberWelfare();
    }

    /**
     * 个人账单
     */
    @ApiOperation(value = "根据类型显示个人账单", notes = "显示用户账单包括消费类型，0全部，1消费，2充值  ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "订单状态", dataType = "BillType", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @GetMapping("/bill")
    public List<CustomerBillDto> showBills(@RequestParam Integer type){
        BillType billType=BillType.getEnum(type).get();
        return customerConsumeService.getBillList(billType);
    }


    /**
     * 用户信息
     */
    @ApiOperation(value = "获取用户信息", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @GetMapping("/info")
    public CustomerDto getCustomerInfo(){
        Customer customer=CurrentCustomerUtil.getCustomer();
        return BeanUtil.mapper(customer,CustomerDto.class);
    }

}
