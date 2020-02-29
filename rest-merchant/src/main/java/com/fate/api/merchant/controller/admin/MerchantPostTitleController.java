package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.MerchantPostTitleDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.MerchantPostTitleQuery;
import com.fate.api.merchant.service.MerchantPostTitleService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 员工职称相关
 * @author:
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - merchantUser", description = "员工职称相关")
@RestController
@RequestMapping("/api/admin/merchant/posttitle")
@Slf4j
public class MerchantPostTitleController {

    @Autowired
    private MerchantPostTitleService merchantPostTitleService;


    /**
     * 保存职称
     */
    @ApiOperation(value = "保存职称", notes = "")
    @ApiImplicitParam(name = "merchantPostTitleQuery", value = "参数对象", required = true, dataType = "MerchantPostTitleQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/save")
    public StandardResponse savePostTitle(@RequestBody @Validated MerchantPostTitleQuery merchantPostTitleQuery) {
         merchantPostTitleService.savePostTitle(merchantPostTitleQuery);
         return StandardResponse.success();
    }

    /**
     * 修改职称
     */
    @ApiOperation(value = "修改职称", notes = "")
    @ApiImplicitParam(name = "merchantPostTitleQuery", value = "参数对象", required = true, dataType = "MerchantPostTitleQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/update")
    public StandardResponse updatePostTitle(@RequestBody @Validated MerchantPostTitleQuery merchantPostTitleQuery) {
         merchantPostTitleService.updatePostTitle(merchantPostTitleQuery);
         return StandardResponse.success();

    }

    /**
     * 删除职称
     */
    @ApiOperation(value = "删除职称", notes = "职称管理-删除")
    @ApiImplicitParam(name = "id", value = "职称id", required = true, dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/delete")
    public StandardResponse deletePostTitle(Long id) {
        merchantPostTitleService.deletePostTitle(id);
        return StandardResponse.success();
    }


    /**
     * 查询职称列表
     */
    @ApiOperation(value = "查询职称列表", notes = "职称列表-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", required = false, dataType = "Integer", paramType = "query")

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public PageDto<MerchantPostTitleDto> getPostTitleList(@RequestParam(defaultValue = "1", required = false) Integer pageIndex,
                                    @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return  merchantPostTitleService.queryPostTitle(pageIndex,pageSize);

    }


}
