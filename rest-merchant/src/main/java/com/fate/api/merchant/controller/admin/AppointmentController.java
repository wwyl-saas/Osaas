package com.fate.api.merchant.controller.admin;

import com.fate.api.merchant.dto.AppointmentDto;
import com.fate.api.merchant.dto.AppointmentSelectDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.AppointmentChangeQuery;
import com.fate.api.merchant.service.AppointmentService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: parent
 * @description: 预约相关
 * @author:
 * @create: 2019-07-18 17:01
 **/
@Api(value = "API - appointment", description = "预约相关")
@RestController("adminAppointmentController")
@RequestMapping("/api/admin/appointment")
@Slf4j
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;


    /**
     * 取消预约
     */
    @ApiOperation(value="取消预约", notes="")
    @ApiImplicitParam(name = "appointmentId", value = "预约Id", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/cancel")
    public StandardResponse cancelAppointment(@RequestParam Long appointmentId){
        appointmentService.cancel(appointmentId);
        return StandardResponse.success();
    }

    /**
     * 确认预约
     */
    @ApiOperation(value="确认预约", notes="")
    @ApiImplicitParam(name = "appointmentId", value = "预约Id", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/confirm")
    public StandardResponse confirmAppointment(@RequestParam Long appointmentId){
        appointmentService.confirm(appointmentId);
        return StandardResponse.success();
    }

    /**
     * 改约提交
     */
    @ApiOperation(value="改约提交", notes="")
    @ApiImplicitParam(name = "query", value = "改约查询对象", required = true, dataType = "AppointmentChangeQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/change")
    public AppointmentDto changeAppointment(@RequestBody @Validated AppointmentChangeQuery query){
        return appointmentService.changeAppointment(query);
    }



    /**
     * 根据门店，技师，开始日期，结束日期,预约状态查询预约列表
     */
    @ApiOperation(value="查询预约列表", notes="预约页-获取预约列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺Id", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "merchantUserId", value = "技师Id", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "appointStatus", value = "预约状态", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码",  defaultValue = "1",dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页长",  defaultValue = "10",dataType = "Integer", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public  PageDto<AppointmentDto> getAppointmentByDateAndMerchantUserId(@RequestParam(required = false) Long shopId,
                                                   @RequestParam(required = false) Long merchantUserId,
                                                    @RequestParam(required = false) Integer appointStatus,
                                                   @RequestParam(required = false) LocalDate startDate,
                                                   @RequestParam(required = false) LocalDate endDate,
                                                   @RequestParam(required = false,defaultValue = "1") Integer pageIndex,
                                                   @RequestParam(required = false,defaultValue = "10") Integer pageSize
    ){
        return appointmentService.getAppointmentByDateRangeAndMerchantUserId(shopId,merchantUserId,appointStatus,startDate,endDate,pageIndex,pageSize);
    }




}
