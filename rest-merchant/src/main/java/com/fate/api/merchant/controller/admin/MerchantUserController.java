package com.fate.api.merchant.controller.admin;

import com.fate.api.merchant.dto.MerchantUserDto;
import com.fate.api.merchant.dto.MerchantUserIndexDto;
import com.fate.api.merchant.dto.UserCenterDto;
import com.fate.api.merchant.query.MerchantUserQuery;
import com.fate.api.merchant.query.UserUpdateQuery;
import com.fate.api.merchant.service.CommentService;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author:
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - merchantUser", description = "商户员工管理")
@RestController("adminMerchantUser")
@RequestMapping("/api/admin/user")
@Slf4j
public class MerchantUserController {

    @Autowired
    private MerchantUserService merchantUserService;




    /**
     * 根据手机号和名称查询员工列表
     */
    @ApiOperation(value = "员工列表", notes = "员工管理-员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query")


    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public Object getUserList(@RequestParam(defaultValue = "1", required = false) Integer pageIndex,
                            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                            @RequestParam (required = false) String mobile,
                            @RequestParam (required = false) String name){

        return merchantUserService.getByMerchantByMobileAndName(pageIndex, pageSize, mobile,name);
    }




    /**
     * 保存员工
     */
    @ApiOperation(value = "新建员工信息", notes = "")
    @ApiImplicitParam(name = "query", value = "参数对象", required = true, dataType = "MerchantUserQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/save")
    public MerchantUserDto save(@RequestBody @Validated MerchantUserQuery query) {
        return merchantUserService.save(query);
    }


    /**
     * 更新员工信息
     */
    @ApiOperation(value = "更新员工信息", notes = "")
    @ApiImplicitParam(name = "query", value = "参数对象", required = true, dataType = "MerchantUserQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/update")
    public MerchantUserDto update(@RequestBody @Validated MerchantUserQuery query) {
        return merchantUserService.update(query);
    }




    /**
     * 删除员工
     */
    @ApiOperation(value = "删除员工", notes = "员工管理-删除")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "userId", value = "员工id",  required = true, dataType = "Long"),
            @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "Long"),


    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/delete")
    public Object deleteUser(@RequestParam Long userId,Long shopId) {
        merchantUserService.remove(userId, shopId);
        return StandardResponse.success();
    }









}
