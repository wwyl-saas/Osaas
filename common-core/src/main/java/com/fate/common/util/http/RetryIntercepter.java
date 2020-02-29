package com.fate.common.util.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** 请求重试
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-05-08 11:06
 **/
@Slf4j
public class RetryIntercepter implements Interceptor {
    private int maxRetry;//最大重试次数


    public RetryIntercepter(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain){
        Request request = chain.request();
        int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
        //   自定义重试次数设置
        int custRetryNum = request.header(OkHttpConfig.DEFAULT_HEADER_RETRYTIMES) == null ? maxRetry : Integer.parseInt(request.header(OkHttpConfig.DEFAULT_HEADER_RETRYTIMES));
        Response response = doRequest(chain, request);
        while (response == null && retryNum < custRetryNum) {
            log.error(String.format("第%s次请求返回失败,继续重试",retryNum));
            retryNum++;
            response = doRequest(chain, request);
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            log.error("发起http请求失败",e);
        }
        return response;
    }
}