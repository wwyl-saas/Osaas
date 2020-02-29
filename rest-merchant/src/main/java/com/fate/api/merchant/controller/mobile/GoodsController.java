package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.dto.CategoryDto;
import com.fate.api.merchant.dto.SettleDto;
import com.fate.api.merchant.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: parent
 * @description: 商品列表
 * @author: chenyixin
 * @create: 2019-07-18 17:03
 **/
@Api(value = "API - goods", description = "商品列表页面")
@RestController
@RequestMapping("/api/goods")
@Slf4j
public class GoodsController {
    @Autowired
    CategoryService categoryService;
    /**
     * 查询分类列表
     */
    @ApiOperation(value="获取分类商品列表", notes="获取分类信息与所属的商品信息，最长关键字不超过50")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public SettleDto getCategoryList(@RequestParam Long shopId){
        return categoryService.getCategoryDtoList(shopId);
    }




}
