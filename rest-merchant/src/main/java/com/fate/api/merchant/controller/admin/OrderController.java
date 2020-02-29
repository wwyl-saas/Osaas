package com.fate.api.merchant.controller.admin;



import com.fate.api.merchant.dto.OrderDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.service.CustomerAccountService;
import com.fate.api.merchant.service.OrderService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @program: parent
 * @description: 订单相关
 * @author:
 * @create: 2019-07-18 17:01
 **/
@Api(value = "API - appointment", description = "订单相关相关")
@RestController("adminOrderController")
@RequestMapping("/api/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerAccountService customerAccountService;


    /**
     * 会员代扣
     */
    @ApiOperation(value="会员代扣", notes="会员代扣")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单Id", required = true, dataType = "Long" ,paramType = "query"),
            @ApiImplicitParam(name = "memberId", value = "代付会员id", required = true, dataType = "Long" , paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/member/withhold")
    public StandardResponse remainderSettle(@RequestParam Long orderId,@RequestParam Long  memberId) {
        Boolean flag= orderService.memberWithhold(orderId,memberId);
        Assert.isTrue(flag,"代支付失败");
        return StandardResponse.success(flag);

    }

    /**
     * 订单查询
     */
    @ApiOperation(value="根据查询订单状态、订单编号和订单id查询订单", notes="根据查询订单状态、订单编号和订单id查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单Id", required = false, dataType = "Long" ,paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", required = false, dataType = "Long" , paramType = "query"),
            @ApiImplicitParam(name = "orderStatus", value = "订单id", required = false, dataType = "Long" , paramType = "query")

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/get")
    public PageDto getOrderByStatusAndOrderNoAndId(@RequestParam (required = false) Long orderId , @RequestParam (required = false)String  orderNo,
                                                   @RequestParam (required = false) Integer  orderStatus,
                                           @RequestParam(required = false,defaultValue = "1") Long pageIndex,
                                           @RequestParam(required = false,defaultValue = "10") Long pageSize) {
        PageDto PageDto= orderService.getOrderByStatusAndOrderNoAndId(orderId,orderNo,orderStatus,pageIndex,pageSize);
        return PageDto;

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










}
