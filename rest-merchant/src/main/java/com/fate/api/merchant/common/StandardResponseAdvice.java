package com.fate.api.merchant.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fate.common.model.StandardResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * @program: parent
 * @description: 返回值格式化处理
 * @author: chenyixin
 * @create: 2019-04-30 19:49
 **/
@RestControllerAdvice
@Slf4j(topic = "request-log")
public class StandardResponseAdvice implements ResponseBodyAdvice {
    private static final String REQUEST_PARAM = "request-param";
    private static final String RESPONSE_URL = "request-url";
    private static final String ENTITY = "response-entity";
    private static final String STATUS = "status";
    SerializerFeature[] features = new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect};

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    /**
     * 重写返回结果格式
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Object response;
        ServletServerHttpResponse resp  = (ServletServerHttpResponse)serverHttpResponse;
        ServletServerHttpRequest req = (ServletServerHttpRequest)serverHttpRequest;
        String url = req.getServletRequest().getServletPath();
        if (StringUtils.isNotBlank(url) && url.contains("/api/") ){
            if (o!=null ){
                if (o instanceof StandardResponse ||o instanceof String){
                    response=o;
                }else {
                    response= StandardResponse.success(o);
                }
            }
            else {
                response= StandardResponse.success();
            }
        }else {
            response= o;
        }
        logger(req,resp,o);
        return response;
    }

    /**
     * 日志格式
     * @param req
     * @param resp
     * @param body
     */
    private void logger(ServletServerHttpRequest req, ServletServerHttpResponse resp,Object body){
        Map<String,Object> map = Maps.newHashMap();
        StringBuffer url = req.getServletRequest().getRequestURL();
        if (!StringUtils.contains(url.toString(),"api-docs")){
            map.put(RESPONSE_URL,url);
            Map<String,String[]> param=req.getServletRequest().getParameterMap();
            map.put(REQUEST_PARAM,param);
            map.put(STATUS,resp.getServletResponse().getStatus());
            map.put(ENTITY,body);
            log.info(JSON.toJSONString(map,features));
        }
    }
}
