package com.fate.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;

/**
 * @program: parent
 * @description: 查询真实IP
 * @author: chenyixin
 * @create: 2019-05-08 10:04
 **/
@Slf4j
public class IpUtil {
    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.info("获取客户端ip: " + ip);
        return ip;
    }

    public static String logIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
//        logHeaders(request);
        log.info("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            log.info("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            log.info("getRemoteAddr ip: " + ip);
        }
        log.info("获取客户端ip: " + ip);
        return ip;
    }


    public static void logHeaders(HttpServletRequest request) {
        String ip1 = request.getHeader("x-forwarded-for") == null ? "" : request.getHeader("x-forwarded-for");
        String ip2 = request.getHeader("Proxy-Client-IP") == null ? "" : request.getHeader("Proxy-Client-IP");
        String ip3 = request.getHeader("WL-Proxy-Client-IP") == null ? "" : request.getHeader("WL-Proxy-Client-IP");
        String ip4 = request.getHeader("HTTP_CLIENT_IP") == null ? "" : request.getHeader("HTTP_CLIENT_IP");
        String ip5 = request.getHeader("HTTP_X_FORWARDED_FOR") == null ? "" : request.getHeader("HTTP_X_FORWARDED_FOR");
        String ip6 = request.getHeader("X-Real-IP") == null ? "" : request.getHeader("X-Real-IP");
        String ip7 = request.getRemoteAddr() == null ? "" : request.getRemoteAddr();
        StringBuilder sb = new StringBuilder();
        sb.append("==请求IP解析：").append("   ").append("x-forwarded-for").append("=").append(ip1).append(" |")
                .append("x-forwarded-for").append("=").append(ip1).append(" |")
                .append("Proxy-Client-IP").append("=").append(ip2).append(" |")
                .append("WL-Proxy-Client-IP").append("=").append(ip3).append(" |")
                .append("HTTP_CLIENT_IP").append("=").append(ip4).append(" |")
                .append("HTTP_X_FORWARDED_FOR").append("=").append(ip5).append(" |")
                .append("X-Real-IP").append("=").append(ip6).append(" |")
                .append("getRemoteAddr").append("=").append(ip7);
        log.info(sb.toString());
    }


    /**
     * 获取局域网IP
     */
    public static String getHostIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取服务器IP", e);
        }
        return "";
    }

    /**
     * 获取内网IP
     *
     * @return
     */
    public static String getSerIp() {
        String clientIp = "";
        // 根据网卡取本机配置的IP
        Enumeration<NetworkInterface> allNetInterfaces;  //定义网络接口枚举类
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();  //获得网络接口
            InetAddress ip = null; //声明一个InetAddress类型ip地址
            while (allNetInterfaces.hasMoreElements()) //遍历所有的网络接口
            {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses(); //同样再定义网络地址枚举类
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && (ip instanceof Inet4Address)) //InetAddress类包括Inet4Address和Inet6Address
                    {
                        if (!ip.getHostAddress().equals("127.0.0.1")) {
                            clientIp = ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("获取服务器IP", e);
        }
        return clientIp;
    }

    public static void main(String[] args) {
        getHostIp();
        System.out.println(getSerIp());
    }

}
