package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.dto.OrderDto;
import com.fate.api.merchant.dto.OrderListDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.OrderCreateQuery;
import com.fate.api.merchant.service.OrderService;
import com.fate.common.enums.OrderStatus;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

/**
 * @program: parent
 * @description: 下单相关
 * @author: chenyixin
 * @create: 2019-07-18 16:50
 **/
@Api(value = "API - order", description = "下单相关")
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * 扫码下单
     */
    @ApiOperation(value="扫码下单", notes="扫码后商品关联用户，下单并返回结算信息")
    @ApiImplicitParam(name = "query", value = "改约查询对象", required = true, dataType = "OrderCreateQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/barcode/place")
    public OrderDto barcodePlace(@RequestBody @Validated OrderCreateQuery query){
        return orderService.barcodePlace(query);
    }


    /**
     * 代付扫码下单结算
     */
    @ApiOperation(value="代付扫码下单结算", notes="")
    @ApiImplicitParam(name = "query", value = "改约查询对象", required = true, dataType = "OrderCreateQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/qrcode/place")
    public OrderDto qrCodePlace(@RequestBody @Validated OrderCreateQuery query){
        return orderService.qrCodePlace(query);
    }

    /**
     * 获取订单列表
     */
    @ApiOperation(value="获取功能导航列表", notes="管理员、店长查看门店所有订单，店员查看个人订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "查询日期",  dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码",  defaultValue = "1",dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页长",  defaultValue = "10",dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public OrderListDto getOrderList(@RequestParam Long shopId,
                                     @RequestParam LocalDate date,
                                     @RequestParam(required = false) Integer status,
                                     @RequestParam(required = false) String customerPhone,
                                     @RequestParam(required = false,defaultValue = "1") Long pageIndex,
                                     @RequestParam(required = false,defaultValue = "10") Long pageSize){
        OrderStatus orderStatus=null;
        if (status!=null){
            orderStatus=OrderStatus.getEnum(status).get();
        }
        return orderService.getOrderList(shopId,date,orderStatus,customerPhone,pageIndex,pageSize);
    }


    /**
     * 获取订单详情
     */
    @ApiOperation(value="获取订单详情", notes="")
    @ApiImplicitParam(name = "orderId", value = "订单Id", required = true, dataType = "Long" ,paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/detail")
    public OrderDto getDetail(@RequestParam Long orderId){
        return orderService.getOrderDetail(orderId);
    }


    /**
     * 取消订单
     */
    @ApiOperation(value="取消订单", notes="")
    @ApiImplicitParam(name = "orderId", value = "订单Id", required = true, dataType = "Long" ,paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/cancel")
    public StandardResponse cancelOrder(@RequestParam Long orderId){
        orderService.cancle(orderId);
        return StandardResponse.success();
    }



    /**
     * 余额扣款
     */
    @ApiOperation(value="余额扣款", notes="")
    @ApiImplicitParam(name = "orderId", value = "订单Id", required = true, dataType = "Long" ,paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/remainder/settle")
    public OrderDto remainderSettle(@RequestParam Long orderId){
        return orderService.remainderSettle(orderId);
    }




}
