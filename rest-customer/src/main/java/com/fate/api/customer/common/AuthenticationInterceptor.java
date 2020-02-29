package com.fate.api.customer.common;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.JwtToken;
import com.fate.api.customer.service.CustomerService;
import com.fate.api.customer.service.JwtService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerApplication;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
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
    CustomerService customerService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有AUTH认证，没有则跳过认证
        if (method.isAnnotationPresent(Auth.class)) {
            Optional<String> token = Optional.ofNullable(httpServletRequest.getHeader("token"));
            String TokenString=token.orElseThrow(()->new BaseException(ResponseInfo.NO_AUTH));
            JwtToken jwtToken=jwtService.verifierToken(TokenString).orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR));
            //用户信息
            Optional<Customer> customer =Optional.ofNullable(customerService.getByCustomerId(jwtToken.getUserId()));
            CurrentCustomerUtil.addCustomer(customer.orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR)));
            if (method.getAnnotation(Auth.class).mobile()){
                if (StringUtils.isEmpty(customer.get().getMobile())){
                    throw new BaseException(ResponseInfo.NO_MEMBER);
                }
            }
            //用户应用关联信息
            CustomerApplication customerApplication=customerService.getCustomerApplicationByCustomerIdAndApplicationId(customer.get().getId(), CurrentApplicationUtil.getMerchantApplication().getId());
            Assert.notNull(customerApplication,"用户应用信息不能为空");
            CurrentCustomerUtil.addCustomerApplication(customerApplication);
            return true;
        }else {
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
        CurrentCustomerUtil.remove();
    }
}