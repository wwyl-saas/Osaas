package com.fate.common.util.http;

/**
 * @program: parent
 * @description: Okhttp配置管理类
 * @author: chenyixin
 * @create: 2019-05-08 11:24
 **/
public class OkHttpConfig {
    public static final String OKHTTP_LOGGER_NG_TRACE_ID = "NG_TRACE_ID";
    public static final String DEFAULT_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String DEFAULT_HEADER_ACCEPT_ENCODING_VALUE = "identity";
    public static final String DEFAULT_HEADER_RETRYTIMES = "Okhttp-Retry-Times";
    //默认连接池空闲连接数5
    public static final int DEFAULT_CLIENT_MAXIDLECONNECTIONS = 100;
    //默认长连接时长3分钟
    public static final long DEFAULT_CLIENT_KEEPALIVEDURATION = 3;
    //默认客户端连接超时15秒
    public static final long DEFAULT_CLIENT_CONNECTTIMEOUT = 15;
    //默认客户端读取超时20秒
    public static final long DEFAULT_CLIENT_READTIMEOUT = 20;
    //默认客户端写入超时20秒
    public static final long DEFAULT_CLIENT_WRITETIMEOUT = 20;
    //默认客户端重试次数
    public static final int DEFAULT_CLIENT_MAXRETRY = 0;

}
