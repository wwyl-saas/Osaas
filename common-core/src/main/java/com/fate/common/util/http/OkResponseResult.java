package com.fate.common.util.http;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.io.IOException;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-05-08 11:13
 **/
@Data
@Slf4j
public class OkResponseResult {
    public static final String COMMON_ERROR_MSG = "Interceptor com.sq.common.okhttp.intercepter.RetryIntercepter occured request exception.";
    public static final String CONNECTION_TIMEOUT = "Connection timed out:okHttp connect";
    public static final int ERROR_RESPONSE_CODE_IOEXCEPTION = 504;
    public static final int SUCCESS_RESPONSE_CODE = 200;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    private boolean isSuccessful;
    private String responseBody;
    private String errorMsg;

    public static OkResponseResult buildSuccessResponse(String responseBody) {
        log.info("请求成功返回："+responseBody);
        OkResponseResult result = new OkResponseResult();
        result.setSuccessful(true);
        result.setResponseBody(responseBody);
        return result;
    }

    public static OkResponseResult buildFaliureResponse(Response execute) throws IOException {
        OkResponseResult result = new OkResponseResult();
        result.setSuccessful(false);
        if (execute != null) {
            log.info("请求失败："+execute.networkResponse().toString());
            result.setResponseBody(execute.networkResponse().toString());
            result.setErrorMsg(execute.body().string());
        } else {
            log.info("请求失败："+CONNECTION_TIMEOUT);
            result.setResponseBody(CONNECTION_TIMEOUT);
            result.setErrorMsg(COMMON_ERROR_MSG);
        }
        return result;
    }

    public static OkResponseResult buildIOExceptionResponse()  {
        log.info("请求失败："+CONNECTION_TIMEOUT);
        OkResponseResult result = new OkResponseResult();
        result.setSuccessful(false);
        result.setResponseBody(CONNECTION_TIMEOUT);
        result.setErrorMsg(COMMON_ERROR_MSG);
        return result;
    }
}

