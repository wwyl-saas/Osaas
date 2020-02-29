package com.fate.api.merchant.controller.mobile;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @description: 登录相关
 * @author: lpx
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - merchantUser", description = "商户用户管理")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class MerchantUserController {

    @Autowired
    private MerchantUserService merchantUserService;
    @Autowired
    private CommentService commentService;


    /**
     * 员工详情
     */
    @ApiOperation(value = "员工详情", notes = "员工管理-员工详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "员工id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/detail")
    public MerchantUserDto getDetail(@RequestParam Long userId, @RequestParam Long shopId) {
        return merchantUserService.detail(userId, shopId);
    }


    /**
     * 员工列表
     */
    @ApiOperation(value = "员工列表", notes = "员工管理-员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "Long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/list")
    public Object getUserList(@RequestParam(defaultValue = "1", required = false) Integer pageIndex,
                              @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                              @RequestParam Long shopId) {
        return merchantUserService.getUserList(pageIndex, pageSize, shopId);
    }

    /**
     * 员工索引列表
     */
    @ApiOperation(value = "员工索引列表", notes = "员工管理-员工索引列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "门店ID", required = true, dataType = "Long", paramType = "query")
    })

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/index/list")
    public List<MerchantUserIndexDto> getUserIndexList(@RequestParam Long shopId,
                                                       @RequestParam(required = false) String userName) {
        return merchantUserService.getUserIndexList(shopId, userName);
    }


    /**
     * 引入总店员工
     */
    @ApiOperation(value = "员工详情", notes = "员工管理-员工详情")
    @ApiImplicitParam(name = "mobile", value = "员工手机号", required = true, dataType = "String", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/detail/mobile")
    public MerchantUserDto getDetailByMobile(@RequestParam String mobile, @RequestParam Long shopId) {
        return merchantUserService.detailByMobile(mobile, shopId);
    }


    /**
     * 保存员工
     */
    @ApiOperation(value = "保存员工", notes = "员工管理-新增、保存")
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
    @ApiOperation(value = "更新员工", notes = "员工管理-保存")
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
     * 更新员工信息
     */
    @ApiOperation(value = "更新自身部分信息", notes = "个人中心")
    @ApiImplicitParam(name = "query", value = "参数对象", required = true, dataType = "UserUpdateQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/personal/update")
    public StandardResponse updateSelf(@RequestBody UserUpdateQuery query) {
        if (StringUtils.isNotBlank(query.getNickname()) ||
                StringUtils.isNotBlank(query.getProfileUrl()) ||
                StringUtils.isNotBlank(query.getUserBrief()) ||
                query.getIfAppointment() != null) {
            merchantUserService.updateSelf(query);
            return StandardResponse.success();
        }
        return StandardResponse.error(ResponseInfo.PARAM_NULL);
    }


    /**
     * 删除员工
     */
    @ApiOperation(value = "删除员工", notes = "员工管理-删除")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/delete")
    public Object deleteUser(@RequestParam Long userId, @RequestParam Long shopId) {
        merchantUserService.remove(userId, shopId);
        return StandardResponse.success();
    }


    /**
     * 个人中心
     */
    @ApiOperation(value = "个人中心", notes = "个人中心")
    @ApiImplicitParam(name = "shopId", value = "门店id", required = true, dataType = "long", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/personal/center")
    public UserCenterDto personalCenter(@RequestParam Long shopId) {
        return merchantUserService.personalCenter(shopId);
    }


    /**
     * 服务反馈列表
     */
    @ApiOperation(value = "个人中心", notes = "个人中心-服务反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentType", value = "评论类型", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "havePicture", value = "是否有图", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "createDateStart", value = "创建开始时间", required = false, dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "createDateEnd", value = "创建结束时间", required = false, dataType = "LocalDate", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", defaultValue = "1", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", defaultValue = "10", required = false, dataType = "Long", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/comment/list")
    public Object getCommentList(@RequestParam(defaultValue = "1") Long pageIndex,
                                 @RequestParam(defaultValue = "10") Long pageSize,
                                 @RequestParam(required = false) Integer commentType,
                                 @RequestParam(required = false) Integer havePicture,
                                 @RequestParam(required = false) LocalDate createDateStart,
                                 @RequestParam(required = false) LocalDate createDateEnd) {
        return commentService.getCommentList(commentType,havePicture,createDateStart,createDateEnd,pageIndex, pageSize);
    }


    /**
     * 修改密码
     */
    @ApiOperation(value = "个人中心", notes = "个人中心-密码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "psw", value = "新密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "confirmPsw", value = "确认密码", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/change/password")
    public Object changePassword(@RequestParam("psw") String psw, @RequestParam("confirmPsw") String confirmPsw) {
        merchantUserService.changePsw(psw, confirmPsw);
        return StandardResponse.success();
    }

}
