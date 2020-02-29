package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.GoodsDto;
import com.fate.api.merchant.dto.MerchantShopDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.MerchantShopQuery;
import com.fate.api.merchant.service.GoodsService;
import com.fate.api.merchant.service.MerchantShopService;
import com.fate.api.merchant.service.MerchantUserRoleService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 商铺管理相关
 * @author:
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - merchantUser", description = "商铺管理相关")
@RestController
@RequestMapping("/api/admin/merchant/shop")
@Slf4j
public class MerchantShopController {

    @Autowired
    private MerchantShopService merchantShopService;

    @Autowired
    private MerchantUserRoleService merchantUserRoleService;

    @Autowired
    GoodsService goodsService;




    /**
     * 保存门店
     */
    @ApiOperation(value = "门店管理-新增", notes = "门店管理-新增")
    @ApiImplicitParam(name = "merchantShopQuery", value = "参数对象", required = true, dataType = "MerchantPostTitleQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/save")
    public StandardResponse saveMerchantShop(@RequestBody @Validated MerchantShopQuery merchantShopQuery) {
         merchantShopService.saveMerchantShop(merchantShopQuery);
         return StandardResponse.success();
    }

    /**
     * 修改门店信息
     */
    @ApiOperation(value = "门店管理-修改", notes = "门店管理-修改")
    @ApiImplicitParam(name = "merchantShopQuery", value = "参数对象", required = true, dataType = "MerchantPostTitleQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/update")
    public StandardResponse updateMerchantShop(@RequestBody @Validated MerchantShopQuery merchantShopQuery) {
          merchantShopService.updateMerchantShop(merchantShopQuery);
          return StandardResponse.success();

    }

    /**
     * 删除门店
     */
    @ApiOperation(value = "门店管理-删除", notes = "门店管理-删除")
    @ApiImplicitParam(name = "id", value = "门店id", required = true, dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/delete")
    public StandardResponse deleteMerchantShop(Long id) {
        merchantShopService.deleteMerchantShop(id);
        return StandardResponse.success();
    }
    /**
     * 根据ID获取门店信息
     */
    @ApiOperation(value = "门店管理-获取门店详情", notes = "门店管理-查看门店详情")
    @ApiImplicitParam(name = "id", value = "参数对象", required = true, dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/get")
    public MerchantShopDto getMerchantShop(Long id ) {
        return  merchantShopService.queryOneMerchantShop(id);

    }


    /**
     * 查询门店列表
     */
    @ApiOperation(value = "门店管理-查询列表", notes = "门店管理-根据名称或者全部查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "店铺名称",  required = false, dataType = "String", paramType = "query")

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<MerchantShopDto> getShopList(@RequestParam(defaultValue = "1", required = false) Integer pageIndex,
                                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(required = false) String name) {
        return  merchantShopService.queryMerchantShopByName(pageIndex,pageSize,name);

    }

    /**
     * 查询当前商户的产品
     */
    @ApiOperation(value = "门店管理-查询当前商户的产品", notes = "门店管理-查询当前商户的产品")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/goods/list")
    public List<GoodsDto> getShopList() {
        return  goodsService.queryGoodsByMerchant();

    }




}
