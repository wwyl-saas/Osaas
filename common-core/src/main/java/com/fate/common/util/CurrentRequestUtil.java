package com.fate.common.util;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: parent
 * @description: 当前请求信息
 * @author: chenyixin
 * @create: 2019-07-18 01:05
 **/
public class CurrentRequestUtil {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void addRequest(HttpServletRequest request){
        requestHolder.set(request);
    }

    public static HttpServletRequest get(){
        return requestHolder.get();
    }


    public static String getCurrentClientIp(){
        String ip=IpUtil.getIpAddr(requestHolder.get());
        Assert.notNull(ip,"Ip获取异常");
        return ip;
    }

    public static void remove(){
        requestHolder.remove();
    }
}
