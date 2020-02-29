package com.fate.api.customer.controller;


import com.fate.api.customer.dto.CustomerDto;
import com.fate.api.customer.dto.JwtToken;
import com.fate.api.customer.exception.NoAuthException;
import com.fate.api.customer.query.LoginQuery;
import com.fate.api.customer.query.TryLoginQuery;
import com.fate.api.customer.service.JwtService;
import com.fate.api.customer.service.LoginService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: parent
 * @description: 登录相关
 * @author: chenyixin
 * @create: 2019-05-22 11:41
 **/
@Api(value = "API - auth", description = "微信用户登录授权相关接口")
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    private JwtService jwtService;

    /**
     * 尝试后台登录（用户已注册的情况下直接返回用户信息）
     */
    @ApiOperation(value="尝试后台登录（用户已注册的情况下直接返回用户信息）", notes="用户之前已经授权则返回用户信息和token,如果未授权则返回错误信息")
    @ApiImplicitParam(name = "query", value = "参数对象", required = true, dataType = "TryLoginQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/try/login")
    public Map<String,Object> tryLogin(@Validated @RequestBody TryLoginQuery query) {
        Map<String,Object> result=new HashMap<>();
        CustomerDto customer=loginService.tryLogin(query);
        JwtToken token=new JwtToken(customer.getMerchantId(),customer.getId());
        result.put("customer",customer);
        result.put("token",jwtService.createToken(token));
        return result;
    }

    /**
     * 用户授权登录（用户已注册的情况下直接返回用户信息）
     */
    @ApiOperation(value="用户授权登录（用户已注册的情况下直接返回用户信息）", notes="如果用户已经存在，则强制性更新用户信息")
    @ApiImplicitParam(name = "query", value = "参数对象", required = true, dataType = "LoginQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/login")
    public Map<String,Object> login(@Validated @RequestBody LoginQuery query) throws WxErrorException {
        Map<String,Object> result=new HashMap<>();
        CustomerDto customer=loginService.login(query);
        JwtToken token=new JwtToken(customer.getMerchantId(),customer.getId());
        result.put("customer",customer);
        result.put("token",jwtService.createToken(token));
        return result;
    }

}
