package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.dto.AttributeDto;
import com.fate.api.merchant.dto.MerchantShopDto;
import com.fate.api.merchant.dto.NavigatorDto;
import com.fate.api.merchant.service.MerchantService;
import com.fate.api.merchant.service.NavigatorService;
import com.fate.api.merchant.service.StatisticsService;
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
 * @description: 首页
 * @author: chenyixin
 * @create: 2019-07-18 19:14
 **/
@Api(value = "API - index", description = "商户端首页")
@RestController
@RequestMapping("/api/index")
@Slf4j
public class IndexController {
    @Autowired
    NavigatorService navigatorService;
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    MerchantService merchantService;



    /**
     * 获取功能导航列表（菜单权限）
     */
    @ApiOperation(value="获取功能导航列表", notes="不同角色获取列表不同,商户管理员、店长查看门店所有数据")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/navigator/list")
    public List<NavigatorDto> getNavigatorList(@RequestParam Long shopId){
        return navigatorService.getList(shopId);
    }


    /**
     * 获取最近门店
     */
    @ApiOperation(value="获取门店列表", notes="仅获取具有角色的门店列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop/nearest")
    public MerchantShopDto getNearestShop(@RequestParam Double latitude, @RequestParam Double longitude){
        return merchantService.getShopsWithRoleAndNearest(latitude,longitude);
    }

}
