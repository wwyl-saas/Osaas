package com.fate.api.customer.controller;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.AppointmentDto;
import com.fate.api.customer.dto.AppointmentHairdresserDto;
import com.fate.api.customer.dto.AppointmentServiceDto;
import com.fate.api.customer.query.AppointmentCreateQuery;
import com.fate.api.customer.service.AppointmentService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @program: parent
 * @description: 预约相关
 * @author: chenyixin
 * @create: 2019-06-04 23:07
 **/
@Api(value = "API -appointment ", description = "预约相关接口")
@RestController
@RequestMapping("/api/v1/appointment")
@Slf4j
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    /**
     * 获取我的预约列表
     */
    @ApiOperation(value="预约列表接口", notes="个人中心-我的预约")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @GetMapping("/my")
    public List<AppointmentDto> getMyAppointment(){
        return appointmentService.getMyAppointments();
    }

    /**
     * 取消预约
     */
    @ApiOperation(value="取消预约接口", notes="个人中心-我的预约-取消")
    @ApiImplicitParam(name = "appointmentId", value = "预约ID", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @PostMapping("/my/cancel")
    public StandardResponse cancelAppointment(@RequestParam Long appointmentId){
        appointmentService.cancelMyAppointments(appointmentId);
        return StandardResponse.success();
    }


    /**
     * 提交预约信息接口
     */
    @ApiOperation(value="提交预约信息接口", notes="预约页提交预约信息")
    @ApiImplicitParam(name = "appointmentCreateQuery", value = "表单对象", required = true, dataType = "AppointmentCreateQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @PostMapping("/create")
    public StandardResponse createAppointment(@Validated @RequestBody AppointmentCreateQuery appointmentCreateQuery){
        appointmentService.createAppointment(appointmentCreateQuery);
        return StandardResponse.success();
    }


    /**
     * 查询发型师预约列表
     */
    @ApiOperation(value="提交预约信息接口", notes="预约页-获取技师列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺Id", required = true,
                    dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "服务ID", required = false,
                    dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/hairdresser/list")
    public List<AppointmentHairdresserDto> hairdresserList(@RequestParam Long shopId,
                                                           @RequestParam(required = false) Long goodsId){
        return  appointmentService.getHairdresserList(shopId,goodsId);
    }


    /**
     * 查询服务列表
     */
    @ApiOperation(value="查询服务列表", notes="预约页-获取服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺Id", required = true,
                    dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "merchantUserId", value = "技师Id", required = false,
                    dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/service/list")
    public  List<AppointmentServiceDto> serviceList(@RequestParam Long shopId,
                                       @RequestParam(required = false) Long merchantUserId){
        return appointmentService.getServiceList(shopId,merchantUserId);
    }





}
