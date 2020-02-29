package com.fate.api.merchant.common;

import com.fate.common.util.CurrentRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: parent
 * @description: Http过滤器
 * @author: chenyixin
 * @create: 2019-05-22 16:15
 **/
@Order(1)
@WebFilter(urlPatterns = "/api/*",filterName = "apiFilter")
@Slf4j
public class ApiFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain){
        try {
            CurrentRequestUtil.addRequest((HttpServletRequest) servletRequest);
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e){
            log.error("filter报错",e);
        }finally {
            CurrentRequestUtil.remove();
        }
    }




}
