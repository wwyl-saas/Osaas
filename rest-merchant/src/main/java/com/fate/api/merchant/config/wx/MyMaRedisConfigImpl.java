package com.fate.api.merchant.config.wx;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.util.json.WxMaGsonBuilder;
import com.fate.api.merchant.service.CacheService;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @program: parent
 * @description: 自定义的微信小程序配置
 * @author: chenyixin
 * @create: 2019-08-12 17:28
 **/
public class MyMaRedisConfigImpl implements WxMaConfig {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String JSAPI_TICKET = "jsapiTicket";
    private static final String CARD_API_TICKET = "cardApiTicket";
    private static final String HASH_VALUE_FIELD = "value";
    private static final String HASH_EXPIRE_FIELD = "expire";

    private CacheService cacheService;
    private RedissonClient redissonClient;

    /**
     * 微信小程序唯一id，用于拼接存储到redis时的key，防止key重复.
     */
    private String maId;
    private volatile String msgDataFormat;
    private volatile String secret;
    private volatile String aesKey;
    private volatile String httpProxyHost;
    private volatile int httpProxyPort;
    private volatile String httpProxyUsername;
    private volatile String httpProxyPassword;
    protected volatile String appid;
    protected volatile String token;
    private Lock accessTokenLock;
    private Lock jsapiTicketLock;
    private Lock cardApiTicketLock;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void setMaId(String maId) {
        this.maId = maId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMsgDataFormat(String msgDataFormat) {
        this.msgDataFormat = msgDataFormat;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    public void setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
        this.apacheHttpClientBuilder = apacheHttpClientBuilder;
    }

    private volatile ApacheHttpClientBuilder apacheHttpClientBuilder;


    @Override
    public Lock getAccessTokenLock() {
        if (accessTokenLock == null) {
            synchronized (this) {
                if (accessTokenLock == null) {
                    accessTokenLock = redissonClient.getLock(getRedisKey("accessTokenLock"));
                }
            }
        }
        return accessTokenLock;
    }

    @Override
    public String getAccessToken() {
        return getValueFromRedis(ACCESS_TOKEN);
    }

    @Override
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > getExpireFromRedis(ACCESS_TOKEN);
    }

    @Override
    public void expireAccessToken() {
        setExpire(ACCESS_TOKEN, 0);
    }

    @Override
    public void updateAccessToken(WxAccessToken wxAccessToken) {
        updateAccessToken(wxAccessToken.getAccessToken(), wxAccessToken.getExpiresIn());
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        setValueToRedis(ACCESS_TOKEN, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L, accessToken);
    }


    @Override
    public Lock getJsapiTicketLock() {
        if (jsapiTicketLock == null) {
            synchronized (this) {
                if (jsapiTicketLock == null) {
                    jsapiTicketLock = redissonClient.getLock(getRedisKey("jsapiTicketLock"));
                }
            }
        }
        return jsapiTicketLock;
    }


    @Override
    public String getJsapiTicket() {
        return getValueFromRedis(JSAPI_TICKET);
    }

    @Override
    public boolean isJsapiTicketExpired() {
        return System.currentTimeMillis() > getExpireFromRedis(JSAPI_TICKET);
    }

    @Override
    public void expireJsapiTicket() {
        setExpire(JSAPI_TICKET, 0);
    }

    @Override
    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        // 预留200秒的时间
        setValueToRedis(JSAPI_TICKET, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L, jsapiTicket);
    }

    @Override
    public Lock getCardApiTicketLock() {
        if (cardApiTicketLock == null) {
            synchronized (this) {
                if (cardApiTicketLock == null) {
                    cardApiTicketLock = redissonClient.getLock(getRedisKey("cardApiTicketLock"));
                }
            }
        }
        return cardApiTicketLock;
    }

    @Override
    public String getCardApiTicket() {
        return getValueFromRedis(CARD_API_TICKET);
    }

    @Override
    public boolean isCardApiTicketExpired() {
        return System.currentTimeMillis() > getExpireFromRedis(CARD_API_TICKET);
    }

    @Override
    public void expireCardApiTicket() {
        setExpire(CARD_API_TICKET, 0);
    }

    @Override
    public void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
        setValueToRedis(CARD_API_TICKET, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L, cardApiTicket);
    }

    @Override
    public String getAppid() {
        return appid;
    }


    @Override
    public String getSecret() {
        return this.secret;
    }


    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getAesKey() {
        return this.aesKey;
    }

    @Override
    public String getMsgDataFormat() {
        return this.msgDataFormat;
    }

    @Override
    public long getExpiresTime() {
        return getExpireFromRedis(ACCESS_TOKEN);
    }

    @Override
    public String getHttpProxyHost() {
        return this.httpProxyHost;
    }

    @Override
    public int getHttpProxyPort() {
        return this.httpProxyPort;
    }

    @Override
    public String getHttpProxyUsername() {
        return this.httpProxyUsername;
    }

    @Override
    public String getHttpProxyPassword() {
        return this.httpProxyPassword;
    }

    @Override
    public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
        return this.apacheHttpClientBuilder;
    }

    @Override
    public boolean autoRefreshToken() {
        return true;
    }

    @Override
    public String toString() {
        return WxMaGsonBuilder.create().toJson(this);
    }



    /**
     * 获取key的字符串
     *
     * @param key
     * @return
     */
    private String getRedisKey(String key) {
        StringBuilder redisKey = new StringBuilder("maConfig:");
        if (StringUtils.isBlank(maId)) {
            return redisKey.append(key).toString();
        } else {
            return redisKey.append(maId).append(":").append(key).toString();
        }
    }


    /**
     * 获取token的值
     *
     * @param key
     * @return
     */
    private String getValueFromRedis(String key) {
        Object valueObject=cacheService.hget(getRedisKey(key), HASH_VALUE_FIELD);
        if (valueObject!=null){
            return valueObject.toString();
        }
        return null;
    }

    /**
     * 获取token过期时间
     *
     * @param key
     * @return
     */
    private long getExpireFromRedis(String key) {
        Object expireObject=cacheService.hget(getRedisKey(key), HASH_EXPIRE_FIELD);
        if (expireObject!=null){
            String expire = expireObject.toString();
            return Long.parseLong(expire);
        }
        return 0;
    }

    /**
     * 设置token过期时间
     *
     * @param key
     * @param expiresTime
     */
    private void setExpire(String key, long expiresTime) {
        cacheService.hset(getRedisKey(key), HASH_EXPIRE_FIELD, String.valueOf(expiresTime));
    }

    /**
     * 设置值和过期时间
     *
     * @param key
     * @param expiresTime
     * @param value
     */
    private void setValueToRedis(String key, long expiresTime, String value) {
        Map<String, Object> hash = new HashMap<>();
        hash.put(HASH_VALUE_FIELD, value);
        hash.put(HASH_EXPIRE_FIELD, String.valueOf(expiresTime));
        cacheService.hmset(getRedisKey(key), hash);
    }

}
