package com.fate.api.merchant.controller.mobile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.api.merchant.dto.AppointmentDto;
import com.fate.api.merchant.dto.AppointmentListDto;
import com.fate.api.merchant.dto.AppointmentSelectDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.AppointmentChangeQuery;
import com.fate.api.merchant.service.AppointmentService;
import com.fate.common.enums.AppointmentStatus;
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
 * @author: chenyixin
 * @create: 2019-07-18 17:01
 **/
@Api(value = "API - appointment", description = "预约相关")
@RestController
@RequestMapping("/api/appointment")
@Slf4j
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    /**
     * 预约列表
     */
    @ApiOperation(value="获取预约列表", notes="管理员、店长查看门店所有预约，店员查看个人预约")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "shopId", value = "店铺Id", required = true, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "arriveDate", value = "查询日期", required = true,  dataType = "LocalDate", paramType = "query"),
        @ApiImplicitParam(name = "appointmentStatus", value = "预约状态", dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "createDateStart", value = "预约创建时间开始",  dataType = "LocalDate", paramType = "query"),
        @ApiImplicitParam(name = "createDateEnd", value = "预约创建时间结束",  dataType = "LocalDate", paramType = "query"),
        @ApiImplicitParam(name = "appointmentPhone", value = "预约人手机号",  dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "pageIndex", value = "页码",  defaultValue = "1",dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "页长",  defaultValue = "10",dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public AppointmentListDto getAppointmentList(@RequestParam Long shopId,
                                                      @RequestParam LocalDate arriveDate,
                                                      @RequestParam(required = false)Integer appointmentStatus,
                                                      @RequestParam(required = false) LocalDate createDateStart,
                                                      @RequestParam(required = false) LocalDate createDateEnd,
                                                      @RequestParam(required = false) String appointmentPhone,
                                                      @RequestParam(required = false,defaultValue = "1") Integer pageIndex,
                                                      @RequestParam(required = false,defaultValue = "10") Integer pageSize
    ){
        AppointmentStatus status=null;
        if (appointmentStatus!=null){
            status=AppointmentStatus.getEnum(appointmentStatus).get();
        }
        return appointmentService.getAppointmentList(shopId,arriveDate,status,createDateStart,createDateEnd,appointmentPhone,pageIndex,pageSize);
    }

    /**
     * 预约到店时间预先统计
     */
    @ApiOperation(value="预约到店时间预先统计", notes="管理员、店长查看门店所有预约，店员查看个人预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "arriveDate", value = "查询日期", required = true,  dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "createDateStart", value = "预约创建时间开始",  dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "createDateEnd", value = "预约创建时间结束",  dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "appointmentPhone", value = "预约人手机号",  dataType = "String", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/prefilter")
    public List<String> getAppointmentListPrefilter(@RequestParam Long shopId,
                                                          @RequestParam LocalDate arriveDate,
                                                          @RequestParam(required = false) LocalDate createDateStart,
                                                          @RequestParam(required = false) LocalDate createDateEnd,
                                                          @RequestParam(required = false) String appointmentPhone
    ){
        //todo 参数是不是多了
        return appointmentService.getAppointmentListPrefilter(shopId,arriveDate,createDateStart,createDateEnd,appointmentPhone);
    }

    /**
     * 获取特定预约信息
     */
    @ApiOperation(value="获取特定预约信息", notes="")
    @ApiImplicitParam(name = "appointmentId", value = "预约Id", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/one")
    public AppointmentDto getAppointment(@RequestParam Long appointmentId){
        return appointmentService.getAppointment(appointmentId);
    }


    /**
     * 取消预约
     */
    @ApiOperation(value="取消预约", notes="")
    @ApiImplicitParam(name = "appointmentId", value = "预约Id", required = true, dataType = "long", paramType = "query")
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
    @ApiImplicitParam(name = "appointmentId", value = "预约Id", required = true, dataType = "long", paramType = "query")
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
    public List<AppointmentSelectDto> hairdresserList(@RequestParam Long shopId,
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
    public  List<AppointmentSelectDto> serviceList(@RequestParam Long shopId,
                                                   @RequestParam(required = false) Long merchantUserId){
        return appointmentService.getServiceList(shopId,merchantUserId);
    }




}
