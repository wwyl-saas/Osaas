package com.fate.api.merchant.controller;

import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.service.CustomerConsumeService;
import com.fate.api.merchant.service.GoodsService;
import com.fate.api.merchant.service.MerchantService;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.common.entity.Member;
import com.fate.common.entity.Merchant;
import com.fate.common.enums.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: parent
 * @description: 公用下拉列表
 * @author: chenyixin
 * @create: 2019-07-29 11:58
 **/
@Api(value = "API - dropList", description = "下拉列表接口")
@RestController
@RequestMapping("/api/drop/list")
@Slf4j
public class DropListController {
    @Autowired
    MerchantService merchantService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MerchantUserService merchantUserService;

    @Autowired
    CustomerConsumeService customerConsumeService;;


    /**
     * 所有角色列表
     */
    @ApiOperation(value = "角色列表", notes = "角色管理-角色列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/roles")
    public List<SelectListDto> roleList() {
        List<SelectListDto> result = new ArrayList<>();
        for (UserRoleType userRoleType : UserRoleType.values()) {
            SelectListDto selectListDto = SelectListDto.builder().name(userRoleType.getDesc()).code(userRoleType.getCode().longValue()).build();
            result.add(selectListDto);
        }
        return result;
    }

    /**
     * 门店角色列表
     */
    @ApiOperation(value = "门店角色列表", notes = "角色管理-角色列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop/roles")
    public List<SelectListDto> shopRoleList() {
        List<SelectListDto> result = new ArrayList<>();
        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.getCode()>1){
                SelectListDto selectListDto = SelectListDto.builder().name(userRoleType.getDesc()).code(userRoleType.getCode().longValue()).build();
                result.add(selectListDto);
            }
        }
        return result;
    }

    /**
     * 门店员工列表
     */
    @ApiOperation(value = "门店员工列表", notes = "门店员工列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop/users")
    public List<SelectListDto> shopUserList(@RequestParam Long shopId) {
        return merchantUserService.userList(shopId);
    }

    /**
     * 门店服务列表
     */
    @ApiOperation(value = "门店服务列表", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop/services")
    public List<SelectListDto> shopServiceList(@RequestParam Long shopId) {
        return goodsService.serviceList(shopId);
    }

    /**
     * 职称列表
     */
    @ApiOperation(value = "职称列表", notes = "职称列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/positions")
    public List<SelectListDto> positionList() {
        return merchantService.positionList();
    }


    /**
     * 获取门店列表
     */
    @ApiOperation(value="获取门店列表", notes="仅获取具有角色的门店列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shops")
    public List<MerchantShopDto> getShopList(){
        return merchantService.getShopDtoList();
    }


    @ApiOperation(value = "会员等级列表", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/member/level")
    public List<SelectListDto> memberLevelList() {
        List<SelectListDto> result = new ArrayList<>();
        for (MemberLevel memberLevel : MemberLevel.values()) {
            SelectListDto selectListDto = SelectListDto.builder().name(memberLevel.getDesc()).code(memberLevel.getCode().longValue()).build();
            result.add(selectListDto);
        }
        return result;
    }


    @ApiOperation(value = "会员来源列表", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/customer/source")
    public List<SelectListDto> customerSourceList() {
        List<SelectListDto> result = new ArrayList<>();
        for (CustomerSource customerSource : CustomerSource.values()) {
            SelectListDto selectListDto = SelectListDto.builder().name(customerSource.getDesc()).code(customerSource.getCode().longValue()).build();
            result.add(selectListDto);
        }
        return result;
    }

    /**
     * 获取订单状态列表
     */
    @ApiOperation(value="获取订单状态", notes="获取订单状态")
    @GetMapping("/order/status")
    public List<OrderStatusDto> getOrderStatusList(){
        List<OrderStatusDto> result = new ArrayList<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
                OrderStatusDto orderStatusDto = OrderStatusDto.builder().desc(orderStatus.getDesc()).code(orderStatus.getCode()).build();
                result.add(orderStatusDto);

        }
        return result;

    }

    /**
     * 获取预约状态列表
     */
    @ApiOperation(value="获取预约状态", notes="获取预约状态")
    @GetMapping("/appoint/status")
    public List<AppointStatusDto> getAppointStatusList(){
        List<AppointStatusDto> result = new ArrayList<>();
        for (AppointmentStatus appointStatus : AppointmentStatus.values()) {
            AppointStatusDto appointStatusDto = AppointStatusDto.builder().desc(appointStatus.getDesc()).code(appointStatus.getCode()).build();
            result.add(appointStatusDto);
        }
        return result;

    }

    /**
     * 获取商家列表
     */

    @ApiOperation(value="获取商家列表", notes="获取商家列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/merchant/list")
    public List<MerchantDto> getMerchantList(){
        return merchantService.getMerchantIdAndName();


    }

    /**
     * 根据商家查询会员种类列表
     */
    @ApiOperation(value="根据商家查询会员种类列表", notes="根据商家查询会员种类列表")
    @ApiImplicitParam(name = "merchantId", value = "商家ID从界面选择列表得到", required = true, dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/member/list")
    public List<MemberCardDto> getMemberList(@RequestParam(required = true) Long merchantId){
        return customerConsumeService.getMemberIdAndLevel(merchantId);



    }

}
