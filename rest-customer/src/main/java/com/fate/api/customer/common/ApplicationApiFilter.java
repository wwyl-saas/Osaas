package com.fate.api.customer.common;

import com.alibaba.fastjson.JSON;
import com.fate.api.customer.service.MerchantService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.CurrentRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
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
public class ApplicationApiFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            //获取商户信息
            String applicationId=req.getHeader("applicationId");
            Assert.notNull(applicationId,"接口调用applicationId为空");

            MerchantService merchantService =SpringContext.getBean(MerchantService.class);
            Assert.notNull(merchantService,"filter初始化获取bean失败");
            //商户应用
            MerchantApplication merchantApplication=merchantService.findApplicationById(Long.parseLong(applicationId));
//            log.info("获取当前应用信息为："+ JSON.toJSONString(merchantApplication));
            Assert.notNull(merchantApplication,"商户应用不存在");
            Assert.isTrue(merchantApplication.getEnabled(),"商户应用过期");
            CurrentApplicationUtil.addMerchantApplication(merchantApplication);
            //商户信息
            Merchant merchant=merchantService.getById(merchantApplication.getMerchantId());
            Assert.notNull(merchant,"应用商户不存在");
            Assert.isTrue(merchant.getEnabled(),"商户过期");
            CurrentMerchantUtil.addMerchant(merchant);
            //请求信息
            CurrentRequestUtil.addRequest((HttpServletRequest) servletRequest);

            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e){
            throw e;
        }finally {
            // 删除threadlocal信息,防止内存泄漏
            CurrentApplicationUtil.remove();
            CurrentMerchantUtil.remove();
            CurrentRequestUtil.remove();
        }
    }




}
