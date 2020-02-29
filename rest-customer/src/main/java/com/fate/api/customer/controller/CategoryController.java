package com.fate.api.customer.controller;

import com.fate.api.customer.dto.CategoryDto;
import com.fate.api.customer.dto.GoodsDto;
import com.fate.api.customer.service.CategoryService;
import com.fate.common.entity.Goods;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: parent
 * @description: 商品分类相关
 * @author: chenyixin
 * @create: 2019-06-04 23:06
 **/
@Api(value = "API -category ", description = "分类页相关接口")
@RestController
@RequestMapping("/api/v1/category")
@Slf4j
@Validated
public class CategoryController {
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
    public List<CategoryDto> getCategoryList(){
        return categoryService.getCategoryDtoList();
    }

    /**
     * 获取首页商品信息
     */
    @ApiOperation(value="根据关键字搜索信息", notes="目前只支持单独关键字")
    @ApiImplicitParam(name = "keyWords", value = "关键字", required = true,
            dataType = "String", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/search")
    public List<GoodsDto> searchGoods(@RequestParam @Length(max = 50,message = "最长关键字不超过50") String keyWords){
        return categoryService.searchCategoryGoods(keyWords);
    }
}
