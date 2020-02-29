package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.annotation.AuthIgnore;
import com.fate.api.merchant.dto.MerchantUserDto;
import com.fate.api.merchant.query.LoginQuery;
import com.fate.api.merchant.dto.JwtToken;
import com.fate.api.merchant.query.TryLoginQuery;
import com.fate.api.merchant.service.JwtService;
import com.fate.api.merchant.service.LoginService;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.api.merchant.util.SmsUtil;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.BeanUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 登录相关
 * @author: lpx
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - auth", description = "商户用户登录授权相关接口")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    private JwtService jwtService;


    /**
     * 微信直接登录
     *
     * @param query
     * @return
     */
    @ApiOperation(value="当用户手机号登录以后，会绑定其当前微信信息openid和unionId，当用户退出时，解除微信openID和unionId的绑定")
    @ApiImplicitParam(name = "query", value = "微信login时返回的code", required = true, dataType = "TryLoginQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping(path = "/try/login")
    @AuthIgnore
    public StandardResponse tryLogin(@Validated @RequestBody TryLoginQuery query) {
        MerchantUserDto user=loginService.tryLogin(query);
        JwtToken jwtToken = new JwtToken(user.getMerchantId(),user.getId());
        return StandardResponse.success(jwtService.createToken(jwtToken));
    }



    /**
     * 发送验证码接口
     *
     * @param phone
     * @return
     */
    @ApiOperation(value="获取验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping(path = "/sms/send")
    @AuthIgnore
    public StandardResponse send(@RequestParam String phone) {
        if (StringUtils.isNotBlank(phone) && SmsUtil.isMobileNum(phone)) {
            loginService.sendSmsCaptcha(phone);
            return StandardResponse.success();
        }
        return StandardResponse.error("手机号参数有误");
    }



    /**
     * 手机号+验证码登陆接口
     *
     * @param query
     * @return
     */
    @ApiOperation(value="验证码登陆")
    @ApiImplicitParam(name = "query", value = "参数对象", required = true, dataType = "LoginQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping(path = "/login/sms")
    @AuthIgnore
    public Object loginByCaptcha(@Validated @RequestBody LoginQuery query) {
        if (StringUtils.isBlank(query.getSmsCode())) {
            throw new BaseException(ResponseInfo.INVALID_PARAM);
        }

        MerchantUserDto user = loginService.mobileLogin(query);
        JwtToken jwtToken = new JwtToken(user.getMerchantId(),user.getId());
        return StandardResponse.success(jwtService.createToken(jwtToken));
    }


    /**
     * 退出登录且解绑微信
     *
     * @return
     */
    @ApiOperation(value="退出登录且解绑微信")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping(path = "/logout")
    public StandardResponse logout(@RequestHeader String token) {
        loginService.logout();
        jwtService.abolishToken(token);
        return StandardResponse.success();
    }

    /**
     * 用户信息
     */
    @ApiOperation(value="用户信息", notes="员工管理-用户信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/current")
    public MerchantUserDto getCurrent() {
        return BeanUtil.mapper(CurrentUserUtil.getMerchantUser(),MerchantUserDto.class);
    }

}
