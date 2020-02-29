package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.CategoryDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.CategoryQuery;
import com.fate.api.merchant.service.CategoryService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @program: parent
 * @description: 商品分类列表
 * @author: songjinghuan
 * @create: 2019-07-18 17:03
 **/
@Api(value = "API - goods", description = "商品分类页面")
@RestController("categoryController")
@RequestMapping("/api/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * admin查询分类列表
     */
    @ApiOperation(value="获取商品分类列表", notes="获取分类信息，最长关键字不超过50")
    @ApiImplicitParams({
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
    public PageDto<CategoryDto> getCategoryList(@RequestParam(required = false,defaultValue = "1") Integer pageIndex, @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return categoryService.getCategoryListByMercahtIdPage(pageIndex,pageSize);
    }

    /**
     * admin创建分类
     */
    @ApiOperation(value="创建商品分类", notes="创建商品分类")
    @ApiImplicitParam(name = "categoryQuery", value = "商品分类", required = true,
                    dataType = "CategoryQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("create")
    public StandardResponse createCategory(@RequestBody  @Validated CategoryQuery categoryQuery){
        categoryService.insertCategory(categoryQuery);
        return StandardResponse.success();
    }
    /**
     * admin更新商品分类
     */
    @ApiOperation(value="修改商品分类列表", notes="修改商品分类")
    @ApiImplicitParam(name = "categoryQuery", value = "商品分类", required = true,
                    dataType = "CategoryQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("update")
    public StandardResponse updateCategory(@RequestBody  @Validated CategoryQuery categoryQuery){
        categoryService.updateCategory(categoryQuery);
        return StandardResponse.success();
    }

    /**
     * admin删除商品分类
     */
    @ApiOperation(value="删除商品分类列表", notes="删除商品分类")
    @ApiImplicitParam(name = "id", value = "商品分类id", required = true,
            dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/delete")
    public StandardResponse deleteCategory(@RequestParam(required = true) Long id ){
        categoryService.deleteCategory(id);
        return StandardResponse.success();
    }

    /**
     * admin修改分类是否有效
     */
    @ApiOperation(value="修改分类是否有效", notes="修改分类是否有效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品分类Id", required = true,
                    dataType = "Long"),
            @ApiImplicitParam(name = "enable", value = "是否有效", required = true,
                    dataType = "Boolean")

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/status/update")
    public StandardResponse deleteCategory(@RequestParam(required = true) Long id,@RequestParam(required = true)Boolean enable){
        categoryService.updateStatus(id,enable);
        return StandardResponse.success();
    }


}
