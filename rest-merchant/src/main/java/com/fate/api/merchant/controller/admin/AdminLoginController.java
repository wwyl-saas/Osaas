package com.fate.api.merchant.controller.admin;

import com.fate.api.merchant.annotation.AuthIgnore;
import com.fate.api.merchant.dto.JwtToken;
import com.fate.api.merchant.dto.LoginDto;
import com.fate.api.merchant.dto.MerchantUserDto;
import com.fate.api.merchant.query.AdminLoginQuery;
import com.fate.api.merchant.query.LoginQuery;
import com.fate.api.merchant.service.JwtService;
import com.fate.api.merchant.service.LoginService;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: parent
 * @description: 后台登录
 * @author: chenyixin
 * @create: 2019-07-31 23:44
 **/
@Api(value = "API - auth", description = "商户用户登录授权相关接口")
@RestController
@RequestMapping("/api/admin/auth")
@Slf4j
public class AdminLoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    private JwtService jwtService;

    /**
     * 手机号+密码登陆接口
     *
     * @param
     * @return
     */
    @ApiOperation(value="密码登陆")
    @ApiImplicitParam(name = "query", value = "参数对象", required = true, dataType = "AdminLoginQuery")
    @PostMapping(path = "/login/psw" )
    @AuthIgnore
    public LoginDto login(@Validated @RequestBody AdminLoginQuery query) {
        if (StringUtils.isBlank(query.getPwd())) {
            throw new BaseException(ResponseInfo.INVALID_PARAM);
        }
        MerchantUserDto user = loginService.login(query);
        JwtToken jwtToken = new JwtToken(user.getMerchantId(),user.getId());
        LoginDto result=LoginDto.builder().token(jwtService.createToken(jwtToken)).user(user).build();
        return result;
    }
}
