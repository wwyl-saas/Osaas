package com.fate.api.customer.controller;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.OrderDto;
import com.fate.api.customer.query.CommentCreateQuery;
import com.fate.api.customer.service.CommentService;
import com.fate.api.customer.service.OrderService;
import com.fate.common.enums.OrderStatus;
import com.fate.api.customer.dto.PageDto;
import com.fate.common.model.StandardResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @program: parent
 * @description: 订单相关
 * @create: 2019-05-23 08:21
 **/
@Api(value = "API - 订单", description = "下单相关接口")
@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    CommentService  commentService;


    @ApiOperation(value="取消订单", notes="取消订单")
    @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @PostMapping("/close")
    public  StandardResponse closeOrder(@RequestParam  Long orderId) throws WxPayException {
        orderService.cancelOrder(orderId);
        return StandardResponse.success();
    }


    @ApiOperation(value = "历史订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "订单状态", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页面索引", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @GetMapping("/history")
    public PageDto<OrderDto> getOrderHist(@RequestParam(required = false) Integer status,
                                          @RequestParam(required = false,defaultValue = "1")Long pageIndex,
                                          @RequestParam(required = false,defaultValue = "10")Long pageSize){
        return orderService.findOrderHisByCustomer(OrderStatus.getEnum(status).orElse(null),pageIndex,pageSize);
    }



    @ApiOperation(value = "订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @GetMapping("/desc")
    public OrderDto getOrderDesc(@RequestParam Long orderId){
        return orderService.findOrderDescById(orderId);
    }



    @ApiOperation(value = "提交订单评价")
    @ApiImplicitParam(name = "commentCreateQueries", value = "订单ID", required = true, dataType = "CommentCreateQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @PostMapping(value = "/complete/comment")
    public StandardResponse completeOrderCommon(@Validated  @RequestBody CommentCreateQuery commentCreateQueries){
        //todo 好评和带图送优惠券
        commentService.complishOrderComment(commentCreateQueries);
        return StandardResponse.success();
    }



    @ApiOperation(value = "修改订单优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "couponId", value = "优惠券ID", required = true, dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth(mobile = true)
    @PostMapping(value = "/change/coupon")
    public OrderDto changeCoupon(@RequestParam(required = false) Long couponId,@RequestParam Long orderId){
        return orderService.changeCoupon(couponId,orderId);
    }


}
