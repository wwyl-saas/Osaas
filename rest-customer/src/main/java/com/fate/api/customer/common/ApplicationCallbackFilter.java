package com.fate.api.customer.common;

import com.alibaba.fastjson.JSON;
import com.fate.api.customer.service.MerchantService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.CurrentRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: parent
 * @description: 微信回调过滤器
 * @author: chenyixin
 * @create: 2019-06-14 23:34
 **/
@Order(2)
@WebFilter(urlPatterns = "/callback/*",filterName = "callbackFilter")
@Slf4j
public class ApplicationCallbackFilter implements Filter {
    //规定回调接口callback之后第一个目录为applicationId
    private static final String REGEX="callback\\/\\d+";

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            String applicationId=getApplication(request.getRequestURI());
            Assert.notNull(applicationId,"微信接口传回的applicationId解析失败，url="+request.getRequestURI());
            MerchantService merchantService = SpringContext.getBean(MerchantService.class);
            Assert.notNull(merchantService,"filter初始化获取bean失败");
            //商户应用
            MerchantApplication merchantApplication=merchantService.findApplicationById(Long.parseLong(applicationId));
            log.info("获取当前应用信息为："+ JSON.toJSONString(merchantApplication));
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

    /**
     * 正则解析applicationId
     * @param url
     * @return
     */
    private String getApplication(String url){
        if (StringUtils.isNotBlank(url)){
            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()){
                return matcher.group(0).split("/")[1];
            }
        }
        return null;
    }
}
