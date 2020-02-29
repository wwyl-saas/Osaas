package com.fate.common.util;

import com.fate.common.util.http.OkHttpConfig;
import com.fate.common.util.http.OkHttpRequest;
import com.fate.common.util.http.OkResponseResult;
import com.fate.common.util.http.RetryIntercepter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2018/7/31 10:29
 * @Description: OkHttp工具类
 */
@Slf4j
public class OkHttpUtil {
    public static final String GET_ERROR_MSG = "GET request occured exception.";
    public static final String POST_ERROR_MSG = "POST request occured exception.";
    private static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";
    private static final String CHAR_NULL = "";

    private static class OkHttpUtilHolder {
        private static final OkHttpUtil INSTANCE = new OkHttpUtil();
    }

    public static OkHttpUtil getIntance() {
        return OkHttpUtilHolder.INSTANCE;
    }

    private OkHttpUtil() {
    }

    /**
     * 私有的静态成员变量 只声明不创建
     * 私有的构造方法
     * 提供返回实例的静态方法
     * 内部类单例莫斯
     */
    private static OkHttpClient okHttpClient;

    static {

        ConnectionPool connectionPool = new ConnectionPool(OkHttpConfig.DEFAULT_CLIENT_MAXIDLECONNECTIONS, OkHttpConfig.DEFAULT_CLIENT_KEEPALIVEDURATION, TimeUnit.MINUTES);
        okHttpClient = new OkHttpClient.Builder().connectionPool(connectionPool)
                .retryOnConnectionFailure(true)
                .addInterceptor(new RetryIntercepter(OkHttpConfig.DEFAULT_CLIENT_MAXRETRY))
                .connectTimeout(OkHttpConfig.DEFAULT_CLIENT_CONNECTTIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(OkHttpConfig.DEFAULT_CLIENT_WRITETIMEOUT, TimeUnit.SECONDS)
                .readTimeout(OkHttpConfig.DEFAULT_CLIENT_READTIMEOUT, TimeUnit.SECONDS).build();
    }

    /**
     *
     * @param okHttpRequest
     * @return
     */
    public OkResponseResult doGet(OkHttpRequest okHttpRequest) {
        Headers.Builder headerBuilder = buildHeaders(okHttpRequest.headerParams(), okHttpRequest.retryTimes());
        String tag = swapTag(okHttpRequest.tag());
        Request request = new Request.Builder().headers(headerBuilder.build()).url(okHttpRequest.url()).tag(tag).build();
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            } else {
                return OkResponseResult.buildFaliureResponse(execute);
            }
        } catch (Exception e) {
            log.error(GET_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }

    /**
     * 含有getParams
     * @param okHttpRequest
     * @return
     */
    public OkResponseResult doGetByMap(OkHttpRequest okHttpRequest) {
        Headers.Builder headerBuilder = buildHeaders(okHttpRequest.headerParams(), okHttpRequest.retryTimes());
        String tag = swapTag(okHttpRequest.tag());
        Request request = new Request.Builder().headers(headerBuilder.build()).url(buildGetURL(okHttpRequest.url(), okHttpRequest.getParams())).tag(tag).build();
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            } else {
                return OkResponseResult.buildFaliureResponse(execute);
            }
        } catch (Exception e) {
            log.error(GET_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }

    /**
     *
     * @param okHttpRequest
     * @return
     */
    public OkResponseResult doPost(OkHttpRequest okHttpRequest) {
        Headers.Builder headerBuilder = buildHeaders(okHttpRequest.headerParams(), okHttpRequest.retryTimes());
        FormBody.Builder formBuilder = buildForm(okHttpRequest.formParams());
        //转换tag
        String tag = swapTag(okHttpRequest.tag());
        //创建Request
        Request request = new Request.Builder().headers(headerBuilder.build()).url(okHttpRequest.url()).post(formBuilder.build()).tag(tag).build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            }
            return OkResponseResult.buildFaliureResponse(execute);
        } catch (Exception e) {
            log.error(POST_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }

    /**
     *
     * @param okHttpRequest
     * @return
     */
    public OkResponseResult doPostJson(OkHttpRequest okHttpRequest) {
        Headers.Builder headerBuilder = buildHeaders(okHttpRequest.headerParams(), okHttpRequest.retryTimes());
        RequestBody requestBody = FormBody.create(MediaType.parse(MEDIA_TYPE_JSON)
                , okHttpRequest.jsonString());
        String tag = swapTag(okHttpRequest.tag());
        Request request = new Request.Builder()
                .headers(headerBuilder.build())
                .url(okHttpRequest.url())
                .post(requestBody)
                .tag(tag)
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            }
            return OkResponseResult.buildFaliureResponse(execute);
        } catch (Exception e) {
            log.error(POST_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }

    /**
     * 异步GET请求
     *
     * @param okHttpRequest
     * @param callback
     */
    public void doGetAsync(OkHttpRequest okHttpRequest, Callback callback) {
        Headers.Builder headerBuilder = buildHeaders(okHttpRequest.headerParams(), okHttpRequest.retryTimes());
        String tag = swapTag(okHttpRequest.tag());
        Request request = new Request.Builder().headers(headerBuilder.build()).url(buildGetURL(okHttpRequest.url(), okHttpRequest.getParams())).tag(tag).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 异步post请求
     *
     * @param okHttpRequest 请求参数
     * @param callback      回调方法
     */
    public void doPostAsync(OkHttpRequest okHttpRequest, Callback callback) {
        Headers.Builder headerBuilder = buildHeaders(okHttpRequest.headerParams(), okHttpRequest.retryTimes());
        FormBody.Builder builder = buildForm(okHttpRequest.formParams());
        postAsync(okHttpRequest.url(), headerBuilder.build(), builder.build(), callback, okHttpRequest.tag());
    }

    /**
     * 异步post json请求
     *
     * @param okHttpRequest 请求参数
     * @param callback      回调方法
     */
    public void doPostJsonAsync(OkHttpRequest okHttpRequest, Callback callback) {
        Headers.Builder headerBuilder = buildHeaders(okHttpRequest.headerParams(), okHttpRequest.retryTimes());
        RequestBody requestBody = FormBody.create(MediaType.parse(MEDIA_TYPE_JSON)
                , okHttpRequest.jsonString());
        postAsyncJson(okHttpRequest.url(), headerBuilder.build(), requestBody, callback, okHttpRequest.tag());
    }


    /**
     * 可传map，方法自动拼接get请求URL地址
     * @param url
     * @param headerParams
     * @param tag
     * @return
     */
    public OkResponseResult doGet(String url, Map<String, ?> headerParams, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        tag = swapTag(tag);
        Request request = new Request.Builder().headers(headerBuilder.build()).url(url).tag(tag[0]).build();
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            } else {
                return OkResponseResult.buildFaliureResponse(execute);
            }
        } catch (Exception e) {
            log.error(GET_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }

    /**
     * 可传map，方法自动拼接get请求URL地址
     * @param url
     * @param headerParams
     * @param getParams
     * @param tag
     * @return
     */
    public OkResponseResult doGet(String url, Map<String, ?> headerParams, Map<String, ?> getParams, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        tag = swapTag(tag);
        Request request = new Request.Builder().headers(headerBuilder.build()).url(buildGetURL(url, getParams)).tag(tag[0]).build();
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            } else {
                return OkResponseResult.buildFaliureResponse(execute);
            }
        } catch (Exception e) {
            log.error(GET_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }

    /**
     * 同步post请求
     *
     * @param url          请求地址
     * @param headerParams 请求头参数
     * @param formParams   请求参数
     * @return 超时未返回，会返回错误提示
     */
    public OkResponseResult doPost(String url, Map<String, ?> headerParams, Map<String, ?> formParams, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        FormBody.Builder formBuilder = buildForm(formParams);
        //转换tag
        tag = swapTag(tag);
        //创建Request
        Request request = new Request.Builder().headers(headerBuilder.build()).url(url).post(formBuilder.build()).tag(tag[0]).build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            }
            return OkResponseResult.buildFaliureResponse(execute);
        } catch (Exception e) {
            log.error(POST_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }


    /**
     * 同步post请求
     *
     * @param url          请求地址
     * @param headerParams 请求头参数
     * @param params       请求数据参数
     * @param params       请求文件参数
     * @return 超时未返回，会返回错误提示
     */
    public OkResponseResult doPostMultiPart(String url, Map<String, ?> headerParams, Map<String, String> params, Map<String, File> files, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        MultipartBody.Builder multipartBuilder =buildMultipart(params,files);
        MultipartBody multipartBody=multipartBuilder.build();
        //转换tag
        tag = swapTag(tag);
        //创建Request
        Request request = new Request.Builder().headers(headerBuilder.build()).url(url).post(multipartBuilder.build()).tag(tag[0]).build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            }
            return OkResponseResult.buildFaliureResponse(execute);
        } catch (Exception e) {
            log.error(POST_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }

    /**
     * 同步post json请求
     *
     * @param url          请求地址
     * @param headerParams 请求头参数
     * @param jsonString   请求参数
     * @return 超时未返回，会返回错误提示
     */
    public OkResponseResult doPostJson(String url, Map<String, ?> headerParams, String jsonString, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        RequestBody requestBody = FormBody.create(MediaType.parse(MEDIA_TYPE_JSON)
                , jsonString);
        tag = swapTag(tag);
        Request request = new Request.Builder()
                .headers(headerBuilder.build())
                .url(url)
                .post(requestBody)
                .tag(tag[0])
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return OkResponseResult.buildSuccessResponse(execute.body().string());
            }
            return OkResponseResult.buildFaliureResponse(execute);
        } catch (Exception e) {
            log.error(POST_ERROR_MSG, e);
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
            return OkResponseResult.buildIOExceptionResponse();
        }
    }


    /**
     * 异步GET请求
     *
     * @param url
     * @param callback
     */
    public void doGetAsync(String url, Callback callback, Map<String, ?> headerParams, Map<String, ?> getParams, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        tag = swapTag(tag);
        Request request = new Request.Builder().headers(headerBuilder.build()).url(buildGetURL(url, getParams)).tag(tag[0]).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 异步post请求
     *
     * @param url          请求地址
     * @param headerParams 请求头参数
     * @param formParams   请求参数
     * @param callback     回调方法
     */
    public void doPostAsync(String url, Map<String, ?> headerParams, Map<String, ?> formParams, Callback callback, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        FormBody.Builder builder = buildForm(formParams);
        postAsync(url, headerBuilder.build(), builder.build(), callback, tag);
    }

    /**
     * 异步post json请求
     *
     * @param url          请求地址
     * @param headerParams 请求头参数
     * @param jsonString   请求参数
     * @param callback     回调方法
     */
    public void doPostJsonAsync(String url, Map<String, ?> headerParams, String jsonString, Callback callback, String... tag) {
        Headers.Builder headerBuilder = buildHeaders(headerParams);
        RequestBody requestBody = FormBody.create(MediaType.parse(MEDIA_TYPE_JSON)
                , jsonString);
        postAsyncJson(url, headerBuilder.build(), requestBody, callback, tag);
    }

    /**
     * 异步post请求
     *
     * @param url      请求地址
     * @param headers  请求头参数
     * @param formBody 请求参数
     * @param callback 回调方法
     */
    private void postAsync(String url, Headers headers, FormBody formBody, Callback callback, String... tag) {
        tag = swapTag(tag);
        Request request = new Request.Builder().headers(headers).url(url).post(formBody).tag(tag[0]).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 异步post请求
     *
     * @param url         请求地址
     * @param headers     请求头参数
     * @param requestBody 请求参数
     * @param callback    回调方法
     */
    private void postAsyncJson(String url, Headers headers, RequestBody requestBody, Callback callback, String... tag) {
        tag = swapTag(tag);
        Request request = new Request.Builder().headers(headers).url(url).post(requestBody).tag(tag[0]).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     *
     * @param headerParams
     * @return
     */
    private Headers.Builder buildHeaders(Map<String, ?> headerParams) {
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add(OkHttpConfig.DEFAULT_HEADER_ACCEPT_ENCODING, OkHttpConfig.DEFAULT_HEADER_ACCEPT_ENCODING_VALUE);
        headerBuilder.add(OkHttpConfig.OKHTTP_LOGGER_NG_TRACE_ID, (MDC.get(OkHttpConfig.OKHTTP_LOGGER_NG_TRACE_ID) == null) ? CHAR_NULL : MDC.get(OkHttpConfig.OKHTTP_LOGGER_NG_TRACE_ID));
        if (headerParams != null) {
            for (Map.Entry<String, ?> headerEntry : headerParams.entrySet()) {
                if (headerEntry.getValue() != null) {
                    headerBuilder.add(headerEntry.getKey(), getValueEncoded(headerEntry.getValue().toString()));
                } else {
                    headerBuilder.add(headerEntry.getKey(), CHAR_NULL);
                }
            }
        }
        return headerBuilder;
    }

    /**
     * @param headerParams
     * @param retryTimes
     * @return
     */
    private Headers.Builder buildHeaders(Map<String, ?> headerParams, Integer retryTimes) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (retryTimes != null) {
            headerBuilder.add(OkHttpConfig.DEFAULT_HEADER_RETRYTIMES, String.valueOf(retryTimes));
        }
        headerBuilder.add(OkHttpConfig.DEFAULT_HEADER_ACCEPT_ENCODING, OkHttpConfig.DEFAULT_HEADER_ACCEPT_ENCODING_VALUE);
        headerBuilder.add(OkHttpConfig.OKHTTP_LOGGER_NG_TRACE_ID, (MDC.get(OkHttpConfig.OKHTTP_LOGGER_NG_TRACE_ID) == null) ? CHAR_NULL : MDC.get(OkHttpConfig.OKHTTP_LOGGER_NG_TRACE_ID));
        if (headerParams != null) {
            for (Map.Entry<String, ?> headerEntry : headerParams.entrySet()) {
                if (headerEntry.getValue() != null) {
                    headerBuilder.add(headerEntry.getKey(), getValueEncoded(headerEntry.getValue().toString()));
                } else {
                    headerBuilder.add(headerEntry.getKey(), CHAR_NULL);
                }
            }
        }
        return headerBuilder;
    }

    /**
     * @param formParams
     * @return
     */
    private FormBody.Builder buildForm(Map<String, ?> formParams) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (formParams != null) {
            for (String key : formParams.keySet()) {
                if (formParams.get(key) != null) {
                    formBuilder.add(key, formParams.get(key).toString());
                } else {
                    formBuilder.add(key, CHAR_NULL);
                }

            }
        }
        return formBuilder;
    }

    /**
     * 构建MultiPart对象
     * @param params
     * @return
     */
    private MultipartBody.Builder buildMultipart(Map<String, ?> params,Map<String,File> files) {
        MultipartBody.Builder multipart = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (MapUtils.isNotEmpty(files)) {
            for (String key : files.keySet()) {
                if (files.get(key) != null) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), files.get(key));
                    multipart.addFormDataPart("application/octet-stream",key, fileBody);
                }
            }
        }
        if (MapUtils.isNotEmpty(params)){
            for (String key : params.keySet()) {
                if (files.get(key) != null) {
                    multipart.addFormDataPart(key, params.get(key).toString());
                }else {
                    multipart.addFormDataPart(key, CHAR_NULL);
                }
            }
        }
        return multipart;
    }

    /**
     * @param value
     * @return
     */
    private String getValueEncoded(String value) {
        if (value == null) {
            return CHAR_NULL;
        }
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return newValue;
    }

    /**
     * @param url
     * @param params
     * @return
     */
    private String buildGetURL(String url, Map<String, ?> params) {
        StringBuilder builder = null;
        if (params != null) {
            for (String key : params.keySet()) {
                if (key != null && params.get(key) != null) {
                    if (builder == null) {
                        builder = new StringBuilder(url);
                        builder.append((url.indexOf("?") == -1) ? "?" : "&");
                    } else {
                        builder.append("&");
                    }
                    builder.append(key).append("=");
                    if (params.get(key) != null) {
                        builder.append(encodeParams(params.get(key).toString()));
                    } else {
                        builder.append(CHAR_NULL);
                    }
                }
            }
        }
        builder = (builder != null) ? builder : new StringBuilder(url);

        return builder.toString();
    }

    private String[] swapTag(String... tag) {
        tag = tag.length == 0 ? new String[1] : tag;
        tag[0] = tag[0] == null ? CHAR_NULL : tag[0];
        return tag;
    }

    private String swapTag(String tag) {
        tag = tag == null ? CHAR_NULL : tag;
        return tag;
    }

    private String encodeParams(String params) {
        try {
            params = URLEncoder.encode(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return params;
    }
}