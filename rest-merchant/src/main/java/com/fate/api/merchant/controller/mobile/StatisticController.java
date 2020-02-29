package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.service.StatisticsService;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.entity.MerchantUser;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: parent
 * @description: 统计相关
 * @author: chenyixin
 * @create: 2019-07-23 15:27
 **/
@Api(value = "API - statistics", description = "统计相关")
@RestController
@RequestMapping("/api/statistic")
@Slf4j
public class StatisticController {
    @Autowired
    StatisticsService statisticsService;


    /**
     * 获取门店数据
     */
    @ApiOperation(value = "获取门店数据", notes = "不同角色获取数据范围不同（商户/门店）")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop/today/data")
    public List<DataAttributeDto> getShopStatistic(@RequestParam Long shopId) {
        return statisticsService.getShopStatistic(shopId);
    }

    /**
     * 获取门店个人数据
     */
    @ApiOperation(value = "获取门店个人数据", notes = "不同角色获取数据范围不同（商户/门店）")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/user/today/data")
    public List<DataAttributeDto> getUserStatistic(@RequestParam Long shopId) {
        MerchantUser merchantUser = CurrentUserUtil.getMerchantUser();
        return statisticsService.getUserStatistic(shopId, merchantUser.getId());
    }


    /**
     * 获取门店明细数据
     */
    @ApiOperation(value = "获取门店个人数据", notes = "不同角色获取数据范围不同（商户/门店）")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop/list")
    public StatisticListDto getShopStatisticList(@RequestParam Long shopId,
                                                 @RequestParam Integer dataType,
                                                 @RequestParam String dateType,
                                                 @RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate,
                                                 @RequestParam(defaultValue = "1") Integer pageIndex,
                                                 @RequestParam(defaultValue = "30") Integer pageSize) {
        return statisticsService.getShopStatisticList(shopId, dataType, dateType, startDate, endDate,pageIndex,pageSize);
    }

    /**
     * 获取门店趋势图数据
     */
    @ApiOperation(value = "获取门店个人数据", notes = "不同角色获取数据范围不同（商户/门店）")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/shop/chart")
    public List<StatisticDataDto> getShopStatisticChart(@RequestParam Long shopId,
                                                        @RequestParam Integer dataType,
                                                        @RequestParam String dateType,
                                                        @RequestParam LocalDate startDate,
                                                        @RequestParam LocalDate endDate) {
        return statisticsService.getShopStatisticChart(shopId, dataType, dateType, startDate, endDate);
    }


    /**
     * 获取门店个人明细数据
     */
    @ApiOperation(value = "获取门店个人数据", notes = "不同角色获取数据范围不同（商户/门店）")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/user/list")
    public StatisticListDto getUserStatisticList(@RequestParam Long shopId,
                                                 @RequestParam Integer dataType,
                                                 @RequestParam String dateType,
                                                 @RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate,
                                                 @RequestParam(defaultValue = "1") Integer pageIndex,
                                                 @RequestParam(defaultValue = "30") Integer pageSize) {
        return statisticsService.getUserStatisticList(shopId, dataType, dateType, startDate, endDate,pageIndex,pageSize);
    }

    /**
     * 获取门店个人趋势图数据
     */
    @ApiOperation(value = "获取门店个人数据", notes = "不同角色获取数据范围不同（商户/门店）")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/user/chart")
    public List<StatisticDataDto> getUserStatisticChart(@RequestParam Long shopId,
                                                        @RequestParam Integer dataType,
                                                        @RequestParam String dateType,
                                                        @RequestParam LocalDate startDate,
                                                        @RequestParam LocalDate endDate) {
        return statisticsService.getUserStatisticChart(shopId, dataType, dateType, startDate, endDate);
    }


    /**
     * 获取个人关键数据
     */
    @ApiOperation(value = "获取个人关键数据", notes = "")
    @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/user/index")
    public UserKeyIndexDto getUserStatisticIndex() {
        Long userId=CurrentUserUtil.getMerchantUser().getId();
        return statisticsService.getUserStatisticIndex(userId);
    }


    /**
     * 绩效列表
     */
    @ApiOperation(value="绩效列表", notes="门店级别和商户级别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺Id", required = true,dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "dataType", value = "数据类型",required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "dateType", value = "时间维度",required = true,  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期",required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/rank/list")
    public List<RankDto> getAchievementList(@RequestParam Long shopId,
                                            @RequestParam Integer dataType,
                                            @RequestParam String dateType,
                                            @RequestParam LocalDate startDate,
                                            @RequestParam LocalDate endDate){
        return statisticsService.getRankList(shopId,dataType,dateType,startDate,endDate);
    }

}
