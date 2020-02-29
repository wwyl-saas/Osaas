package com.fate.api.admin.common;

import com.fate.api.admin.annotation.AuthIgnore;
import com.fate.api.admin.dto.JwtToken;
import com.fate.api.admin.service.AdminService;
import com.fate.api.admin.service.JwtService;
import com.fate.api.admin.util.CurrentAdminUtil;
import com.fate.common.entity.Admin;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 登录权限校验
 */
@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    JwtService jwtService;
    @Resource
    AdminService adminService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有AUTH_IGNORE有则跳过认证
        if (method.isAnnotationPresent(AuthIgnore.class)) {
            return true;
        }else {
            Optional<String> token = Optional.ofNullable(httpServletRequest.getHeader("token"));
            String TokenString=token.orElseThrow(()->new BaseException(ResponseInfo.NO_AUTH));
            JwtToken jwtToken=jwtService.verifierToken(TokenString).orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR));
            //用户信息
            Optional<Admin> admin =Optional.ofNullable(adminService.getById(jwtToken.getUserId()));
            CurrentAdminUtil.addAdmin(admin.orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR)));
            return  true;
        }
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {
    }

    /**
     *  任何时候都有清空threadLocal信息
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        CurrentAdminUtil.remove();
    }
}