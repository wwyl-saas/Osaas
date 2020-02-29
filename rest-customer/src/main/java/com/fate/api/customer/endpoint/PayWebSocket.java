package com.fate.api.customer.endpoint;

import com.alibaba.fastjson.JSON;
import com.fate.api.customer.common.SpringContext;
import com.fate.common.cons.RedisKey;
import com.fate.api.customer.dto.JwtToken;
import com.fate.api.customer.exception.AuthException;
import com.fate.api.customer.service.CacheService;
import com.fate.api.customer.service.JwtService;
import com.fate.api.customer.util.CustomerSessionUtil;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.IpUtil;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;

/**
 * @program: parent
 * @description: 支付双工接口(不支持集群部署, 集群部署需要扩展改造)
 * @author: chenyixin
 * @create: 2019-05-23 18:18
 **/
@ServerEndpoint(value = "/websocket/{applicationId}")
@Component
@Slf4j
public class PayWebSocket {

    /**
     * 连接成功时需要1.认证用户身份2.需要保存session和用户的关联信息
     * <p>
     * 支付步骤一 请求长连接
     * 支付步骤二 返回一维码
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("applicationId") Long applicationId, Session session) throws IOException {
        try {
            Assert.notNull(session.getQueryString(), "token参数不能为空");
            Optional<String> token = Optional.of(session.getQueryString().split("=")[1]);
            JwtService jwtService = SpringContext.getBean(JwtService.class);
            JwtToken jwtToken = jwtService.verifierToken(token.get()).orElseThrow(() -> new AuthException());
            //全局唯一长连接客户id,不支持多个客户端同时创建连接
            CacheService cacheService = SpringContext.getBean(CacheService.class);
            String existIp = (String) cacheService.get(RedisKey.CUSTOMER_SOCKET_KEY + jwtToken.getUserId());
            if (StringUtils.isBlank(existIp)) {
                String netIp = getIp();
                cacheService.set(RedisKey.CUSTOMER_SOCKET_KEY + jwtToken.getUserId(), netIp, 90L);//两分钟持续时间，需要不断刷新,nginx限制为90秒
                CustomerSessionUtil.add(jwtToken.getUserId(), session);
                sendObjectMessage(session, StandardResponse.success());
            } else {
                log.error("已经用户的存在socket连接");
                sendObjectMessage(session, StandardResponse.error(ResponseInfo.SESSION_EXIST_ERROR));
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "已经用户的存在socket连接"));
            }
        } catch (AuthException e) {
            log.error("建立socket连接token校验失败", e);
            sendObjectMessage(session, StandardResponse.error(ResponseInfo.TOKEN_ERROR));
            session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "建立socket连接token校验失败"));
        } catch (Exception e) {
            log.error("建立socket连接失败", e);
            sendObjectMessage(session, StandardResponse.error(ResponseInfo.SERVICE_UNAVAILABLE));
            session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "建立socket连接失败"));
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        log.info("前端主动关闭连接");
        CacheService cacheService = SpringContext.getBean(CacheService.class);
        Long customerId = CustomerSessionUtil.getCustomerId(session).orElseThrow(() -> new BaseException(ResponseInfo.SESSION_INFO_ERROR));
        cacheService.del(RedisKey.CUSTOMER_SOCKET_KEY + customerId);
        CustomerSessionUtil.remove(session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("前端传来" + message);
        try {
            Long customerId = CustomerSessionUtil.getCustomerId(session).orElseThrow(() -> new BaseException(ResponseInfo.SESSION_INFO_ERROR));
            CacheService cacheService = SpringContext.getBean(CacheService.class);
            cacheService.expire(RedisKey.CUSTOMER_SOCKET_KEY +  customerId, 90L);//两分钟持续时间，需要不断刷新,nginx限制为90秒
            sendObjectMessage(session, StandardResponse.success(message));
        } catch (Exception e) {
            log.error("刷新连接失败", e);
        }
    }

    /**
     * 发生错误时调用
     *
     * @OnError
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        Long customerId = CustomerSessionUtil.getCustomerId(session).orElseThrow(() -> new BaseException(ResponseInfo.SESSION_INFO_ERROR));
        log.error("socket发生错误：{}，customerId： {}", error, customerId);
    }


    /**
     * 发送Json文本消息
     *
     * @param session
     * @param object
     */
    public void sendObjectMessage(Session session, Object object) {
        try {
            String message = JSON.toJSONString(object);
            session.getAsyncRemote().sendText(message, (result -> {
                if (result.isOK()) {
                    log.debug("异步消息发送成功");
                } else {
                    log.error("异步消息发送失败", result.getException());
                }
            }));
        } catch (Exception e) {
            log.error("异步发送失败", e);
        }
    }

    /**
     * 获取服务器Ip
     * @return
     */
    private String getIp(){
        String netIp;
        Environment environment=SpringContext.getBean(Environment.class);
        if (environment.getActiveProfiles().length>0&&environment.getActiveProfiles()[0].equals("dev")){
            netIp ="127.0.0.1";
        }else{
            netIp =IpUtil.getSerIp();
        }
        return netIp;
    }


}
