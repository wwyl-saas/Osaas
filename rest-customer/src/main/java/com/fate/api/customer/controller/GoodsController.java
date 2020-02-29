package com.fate.api.customer.controller;

import com.fate.api.customer.dto.CommentDto;
import com.fate.api.customer.dto.GoodsDetailDto;
import com.fate.api.customer.dto.PageDto;
import com.fate.api.customer.service.CommentService;
import com.fate.api.customer.service.GoodsService;
import com.fate.api.customer.service.MerchantService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: parent
 * @description: 商品相关
 * @author: chenyixin
 * @create: 2019-06-08 17:20
 **/
@Api(value = "API -goods ", description = "商品相关接口")
@RestController
@RequestMapping("/api/v1/goods")
@Slf4j
@Validated
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    MerchantService merchantService;
    @Autowired
    CommentService commentService;


    /**
     * 查询商品详情
     */
    @ApiOperation(value="查询商品详情", notes="需要登录")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/detail")
    public GoodsDetailDto getGoodsDetail(@RequestParam Long goodsId){
        return goodsService.getGoodsDetail(goodsId);
    }


    /**
     * 查询评论列表
     */
    @ApiOperation(value="查询评论列表", notes="需要登录")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, dataType = "Long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/comment/list")
    public List<CommentDto> getGoodsCommentList(@RequestParam Long goodsId){
        return goodsService.getGoodsCommentList(goodsId);
    }

    /**
     * 查询评论列表
     */
    @ApiOperation(value="查询评论列表", notes="需要登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "评价等级 0好评 1中评 2差评", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "havePicture", value = "是否有图 0无图 1有图", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码",  defaultValue = "1",dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页长",  defaultValue = "20",dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/comments/list")
    public PageDto<CommentDto> getGoodsCommentsList(@RequestParam Long goodsId,
                                                    @RequestParam(required = false) Integer type,
                                                    @RequestParam(required = false) Integer havePicture,
                                                    @RequestParam(required = false,defaultValue = "1") Long pageIndex,
                                                    @RequestParam(required = false,defaultValue = "20") Long pageSize){
        return commentService.getGoodsComments(goodsId,type,havePicture,pageIndex,pageSize);
    }


}
