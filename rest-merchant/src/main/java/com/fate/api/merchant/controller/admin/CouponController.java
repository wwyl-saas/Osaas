package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.CouponDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.CouponQuery;
import com.fate.api.merchant.service.CouponService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 优惠券相关
 * @author:
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - merchantUser", description = "优惠券相关")
@RestController("adminCouponController")
@RequestMapping("/api/admin/coupon")
@Slf4j
public class CouponController {

    @Autowired
    private CouponService couponService;


    /**
     * 保存优惠券
     */
    @ApiOperation(value = "保存优惠券", notes = "优惠券管理-新增、保存")
    @ApiImplicitParam(name = "couponQuery", value = "参数对象", required = true, dataType = "CouponQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/save")
    public StandardResponse saveCoupon(@RequestBody @Validated CouponQuery couponQuery) {
         couponService.saveCoupon(couponQuery);
         return StandardResponse.success();
    }

    /**
     * 修改优惠券
     */
    @ApiOperation(value = "修改优惠券", notes = "优惠券管理-修改")
    @ApiImplicitParam(name = "couponQuery", value = "参数对象", required = true, dataType = "CouponQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/update")
    public StandardResponse updateCoupon(@RequestBody @Validated CouponQuery couponQuery) {
         couponService.updateCoupon(couponQuery);
         return StandardResponse.success();

    }

    /**
     * 删除优惠券
     */
    @ApiOperation(value = "删除优惠券", notes = "优惠券管理-删除")
    @ApiImplicitParam(name = "id", value = "优惠券id", required = true, dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/delete")
    public StandardResponse deleteCoupon(Long id) {
        couponService.deleteCoupon(id);
        return StandardResponse.success();
    }


    /**
     * 查询优惠券列表
     */
    @ApiOperation(value = "查询优惠券列表", notes = "优惠券列表-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "couponName", value = "优惠券名称",  required = false, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<CouponDto> queryCouponByName(@RequestParam(defaultValue = "1", required = false) Integer pageIndex,
                                               @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                               @RequestParam(required = false) String couponName         ) {
        return  couponService.queryCouponByName(pageIndex,pageSize,couponName);

    }


}
