package com.fate.api.merchant.common;

import com.fate.api.merchant.annotation.AddMerchant;
import com.fate.api.merchant.annotation.AuthIgnore;
import com.fate.api.merchant.dto.JwtToken;
import com.fate.api.merchant.service.JwtService;
import com.fate.api.merchant.service.MerchantService;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantUser;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.enums.UserRoleType;
import com.fate.common.exception.BaseException;
import com.fate.common.util.CurrentMerchantUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * 登录权限校验
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    JwtService jwtService;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    MerchantService merchantService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        String path=httpServletRequest.getRequestURI();
        if (path.contains("/api/")){//只有API接口需要验权
            HandlerMethod handlerMethod = (HandlerMethod) object;
            Method method = handlerMethod.getMethod();
            //检查是否有AUTH认证，没有则跳过认证
            if (!method.isAnnotationPresent(AuthIgnore.class)) {
                Optional<String> token = Optional.ofNullable(httpServletRequest.getHeader("token"));// 从 http 请求头中取出 token
                JwtToken jwtToken=jwtService.verifierToken(token.orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR)))
                        .orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR));
                //用户信息
                Optional<MerchantUser> user =Optional.ofNullable(merchantUserService.getByUserId(jwtToken.getUserId()));
                CurrentUserUtil.addMerchantUser(user.orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR)));
                //商户信息
                Merchant merchant=merchantService.getMerchantById(user.get().getMerchantId());
                Assert.notNull(merchant,"商户不存在");
                CurrentMerchantUtil.addMerchant(merchant);
                //用户角色信息
                Map<String, UserRoleType> userRoleTypeMap=merchantUserService.getRolesByUserId(user.get().getId());
                CurrentUserUtil.addShopRoleMap(userRoleTypeMap);
            }else if (method.isAnnotationPresent(AddMerchant.class)){
                Optional<String> merchantId = Optional.ofNullable(httpServletRequest.getHeader("merchantId"));
                Long id=Long.parseLong(merchantId.orElseThrow(()->new BaseException("商户ID不存在")));
                Merchant merchant=merchantService.getMerchantById(id);
                Assert.notNull(merchant,"商户不存在");
                CurrentMerchantUtil.addMerchant(merchant);
            }
        }
        return  true;
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
        CurrentUserUtil.remove();
        CurrentMerchantUtil.remove();
    }
}