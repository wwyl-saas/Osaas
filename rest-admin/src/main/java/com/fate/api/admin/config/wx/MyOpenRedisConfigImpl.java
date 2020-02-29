package com.fate.api.admin.config.wx;

import com.fate.api.admin.service.CacheService;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-10-21 11:48
 **/
public class MyOpenRedisConfigImpl extends WxOpenInMemoryConfigStorage {
    private final static String COMPONENT_VERIFY_TICKET_KEY = "wechat_component_verify_ticket:";
    private final static String COMPONENT_ACCESS_TOKEN_KEY = "wechat_component_access_token:";

    private final static String AUTHORIZER_REFRESH_TOKEN_KEY = "wechat_authorizer_refresh_token:";
    private final static String AUTHORIZER_ACCESS_TOKEN_KEY = "wechat_authorizer_access_token:";
    private final static String JSAPI_TICKET_KEY = "wechat_jsapi_ticket:";
    private final static String CARD_API_TICKET_KEY = "wechat_card_api_ticket:";

    private final CacheService cacheService;
    /**
     * redis 存储的 key 的前缀，可为空
     */
    private String keyPrefix;
    private String componentVerifyTicketKey;
    private String componentAccessTokenKey;
    private String authorizerRefreshTokenKey;
    private String authorizerAccessTokenKey;
    private String jsapiTicketKey;
    private String cardApiTicket;

    public MyOpenRedisConfigImpl(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public MyOpenRedisConfigImpl(CacheService cacheService, String keyPrefix) {
        this.cacheService = cacheService;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public void setComponentAppId(String componentAppId) {
        super.setComponentAppId(componentAppId);
        String prefix = StringUtils.isBlank(keyPrefix) ? "" :
                (StringUtils.endsWith(keyPrefix, ":") ? keyPrefix : (keyPrefix + ":"));
        componentVerifyTicketKey = prefix + COMPONENT_VERIFY_TICKET_KEY.concat(componentAppId);
        componentAccessTokenKey = prefix + COMPONENT_ACCESS_TOKEN_KEY.concat(componentAppId);
        authorizerRefreshTokenKey = prefix + AUTHORIZER_REFRESH_TOKEN_KEY.concat(componentAppId);
        authorizerAccessTokenKey = prefix + AUTHORIZER_ACCESS_TOKEN_KEY.concat(componentAppId);
        this.jsapiTicketKey = JSAPI_TICKET_KEY.concat(componentAppId);
        this.cardApiTicket = CARD_API_TICKET_KEY.concat(componentAppId);
    }

    @Override
    public String getComponentVerifyTicket() {
        Object value=cacheService.get(this.componentVerifyTicketKey);
        if (value!=null){
            return value.toString();
        }
        return null;
    }

    @Override
    public void setComponentVerifyTicket(String componentVerifyTicket) {
        cacheService.set(this.componentVerifyTicketKey, componentVerifyTicket);
    }

    @Override
    public String getComponentAccessToken() {
        Object value=cacheService.get(this.componentAccessTokenKey);
        if (value!=null){
            return value.toString();
        }
        return null;
    }

    @Override
    public boolean isComponentAccessTokenExpired() {
        return  cacheService.getExpire(this.componentAccessTokenKey)< 2;
    }

    @Override
    public void expireComponentAccessToken(){
        cacheService.expire(this.componentAccessTokenKey, 0);
    }

    @Override
    public void updateComponentAccessToken(String componentAccessToken, int expiresInSeconds) {
        cacheService.set(this.componentAccessTokenKey,componentAccessToken, expiresInSeconds - 200);
    }

    private String getKey(String prefix, String appId) {
        return prefix.endsWith(":") ? prefix.concat(appId) : prefix.concat(":").concat(appId);
    }

    @Override
    public String getAuthorizerRefreshToken(String appId) {
        Object value=cacheService.get(this.getKey(this.authorizerRefreshTokenKey, appId));
        if (value!=null){
            return value.toString();
        }
        return null;
    }

    @Override
    public void setAuthorizerRefreshToken(String appId, String authorizerRefreshToken) {
        cacheService.set(this.getKey(this.authorizerRefreshTokenKey, appId), authorizerRefreshToken);
    }

    @Override
    public String getAuthorizerAccessToken(String appId) {
        Object value=cacheService.get(this.getKey(this.authorizerAccessTokenKey, appId));
        if (value!=null){
            return value.toString();
        }
        return null;
    }

    @Override
    public boolean isAuthorizerAccessTokenExpired(String appId) {
        return cacheService.getExpire(this.getKey(this.authorizerAccessTokenKey, appId)) < 2;
    }

    @Override
    public void expireAuthorizerAccessToken(String appId) {
        cacheService.expire(this.getKey(this.authorizerAccessTokenKey, appId), 0);
    }

    @Override
    public void updateAuthorizerAccessToken(String appId, String authorizerAccessToken, int expiresInSeconds) {
        cacheService.set(this.getKey(this.authorizerAccessTokenKey, appId),authorizerAccessToken,expiresInSeconds - 200);
    }

    @Override
    public String getJsapiTicket(String appId) {
        Object value=cacheService.get(this.getKey(this.jsapiTicketKey, appId));
        if (value!=null){
            return value.toString();
        }
        return null;
    }

    @Override
    public boolean isJsapiTicketExpired(String appId) {
        return cacheService.getExpire(this.getKey(this.jsapiTicketKey, appId)) < 2;
    }

    @Override
    public void expireJsapiTicket(String appId) {
        cacheService.expire(this.getKey(this.jsapiTicketKey, appId), 0);
    }

    @Override
    public void updateJsapiTicket(String appId, String jsapiTicket, int expiresInSeconds) {
        cacheService.set(this.getKey(this.jsapiTicketKey, appId), jsapiTicket, expiresInSeconds - 200);
    }

    @Override
    public String getCardApiTicket(String appId) {
        Object value=cacheService.get(this.getKey(this.cardApiTicket, appId));
        if (value!=null){
            return value.toString();
        }
        return null;
    }

    @Override
    public boolean isCardApiTicketExpired(String appId) {
        return cacheService.getExpire(this.getKey(this.cardApiTicket, appId)) < 2;
    }

    @Override
    public void expireCardApiTicket(String appId) {
        cacheService.expire(this.getKey(this.cardApiTicket, appId), 0);
    }

    @Override
    public void updateCardApiTicket(String appId, String cardApiTicket, int expiresInSeconds) {
        cacheService.set(this.getKey(this.cardApiTicket, appId), cardApiTicket, expiresInSeconds - 200);
    }
}
