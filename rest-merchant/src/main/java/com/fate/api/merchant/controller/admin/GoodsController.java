package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.AdminGoodsDto;
import com.fate.api.merchant.dto.GoodsDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.AdminGoodsQuery;
import com.fate.api.merchant.service.GoodsService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




/**
 * @program: parent
 * @description: 商品列表
 * @author: songjinghuan
 * @create: 2019-07-18 17:03
 **/
@Api(value = "API - goods", description = "商品列表页面")
@RestController("adminGoodController")
@RequestMapping("/api/admin/goods")
@Slf4j
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    /**
     * admin获取商品列表
     */
    @ApiOperation(value="获取商品列表", notes="根据关键字，商品名称，是否上架查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyWord", value = "关键词",
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsName", value = "名称",
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "onSale", value = "上架状态",
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "第几页",
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小",
                    dataType = "Integer", paramType = "query")

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<GoodsDto> getGoodsList(@RequestParam(required = false) String keyWord, @RequestParam(required = false) String goodsName, @RequestParam(required = false)  Boolean onSale, @RequestParam(required = false,defaultValue = "1") Integer pageIndex, @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return goodsService.getGoodsBykeyWordAndGoodsNameAndOnSale(keyWord,goodsName,onSale,pageIndex,pageSize);

    }



    /**
     * admin创建商品
     */
    @ApiOperation(value="创建商品", notes="创建商品")
    @ApiImplicitParam(name = "adminGoodsQuery", value = "商品信息，商品属性，商品问题等",
                     dataType = "AdminGoodsQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping ("/create")
    public StandardResponse  createGoods(@RequestBody AdminGoodsQuery adminGoodsQuery){
        goodsService.create(adminGoodsQuery);
        return StandardResponse.success();
    }


    /**
     * 更新商品
     * @param adminGoodsQuery
     * @return
     */
    @ApiOperation(value="更新商品", notes="更新商品")
    @ApiImplicitParam(name = "adminGoodsQuery", value = "商品信息，商品属性，商品问题等",
            dataType = "AdminGoodsQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping ("/update")
    public StandardResponse  updateGoods(@RequestBody AdminGoodsQuery adminGoodsQuery){
        goodsService.updateGoods(adminGoodsQuery);
        return StandardResponse.success();
    }



    /**
     * admin根据id获取商品详情，商品属性和商品问题
     */
    @ApiOperation(value="获取商品详情", notes="获取商品详情")
    @ApiImplicitParam(name = "goodsId", value = "商品id",
            dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping ("/get")
    public AdminGoodsDto getGoodById(@RequestParam(required = true) Long goodsId){
        return goodsService.getGoodDetailsById(goodsId);
    }

    /**
     * admin删除商品
     */
    @ApiOperation(value="删除商品", notes="删除商品")
    @ApiImplicitParam(name = "goodsId", value = "商品id",
            dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping ("/delete")
    public StandardResponse deleteGood(@RequestParam(required = true) Long goodsId){
        goodsService.delete(goodsId);
        return StandardResponse.success();
    }





}
