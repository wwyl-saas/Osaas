package com.fate.api.customer.config.wx;

import com.fate.api.customer.service.CacheService;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;
import me.chanjar.weixin.mp.bean.WxMpHostConfig;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.enums.TicketType;
import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @program: parent
 * @description: 微信公众号自定义配置对象
 * @author: chenyixin
 * @create: 2019-08-12 21:32
 **/
public class MyMpRedisConfigImpl implements WxMpConfigStorage{
    private static final String HASH_VALUE_FIELD = "value";
    private static final String HASH_EXPIRE_FIELD = "expire";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String JSAPI_TICKET = "jsapiTicket";
    private static final String CARD_API_TICKET = "cardApiTicket";
    private static final String SDK_TICKET = "sdkTicket";
    
    protected volatile String appId;
    protected volatile String secret;
    protected volatile String token;
    protected volatile String templateId;
    protected volatile String accessToken;
    protected volatile String aesKey;
    protected volatile long expiresTime;

    protected volatile String oauth2redirectUri;

    protected volatile String httpProxyHost;
    protected volatile int httpProxyPort;
    protected volatile String httpProxyUsername;
    protected volatile String httpProxyPassword;

    protected Lock accessTokenLock;
    protected Lock jsapiTicketLock ;
    protected Lock sdkTicketLock ;
    protected Lock cardApiTicketLock;

    private String mpId;
    protected volatile File tmpDirFile;
    protected volatile ApacheHttpClientBuilder apacheHttpClientBuilder;
    private CacheService cacheService;
    private RedissonClient redissonClient;

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public void setOauth2redirectUri(String oauth2redirectUri) {
        this.oauth2redirectUri = oauth2redirectUri;
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

    public void setMpId(String mpId) {
        this.mpId = mpId;
    }

    public void setTmpDirFile(File tmpDirFile) {
        this.tmpDirFile = tmpDirFile;
    }

    public void setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
        this.apacheHttpClientBuilder = apacheHttpClientBuilder;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String getAccessToken() {
        return getValueFromRedis(ACCESS_TOKEN);
    }

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
    public Lock getCardApiLock() {
        if (cardApiTicketLock == null) {
            synchronized (this) {
                if (cardApiTicketLock == null) {
                    cardApiTicketLock = redissonClient.getLock(getRedisKey("cardApiLock"));
                }
            }
        }
        return cardApiTicketLock;
    }
    public Lock getSdkTicketLock() {
        if (sdkTicketLock == null) {
            synchronized (this) {
                if (sdkTicketLock == null) {
                    sdkTicketLock = redissonClient.getLock(getRedisKey("sdkTicketLock"));
                }
            }
        }
        return sdkTicketLock;
    }


    @Override
    public Lock getTicketLock(TicketType ticketType) {
        switch (ticketType) {
            case SDK:
                return getSdkTicketLock();
            case JSAPI:
                return getJsapiTicketLock();
            case WX_CARD:
                return getCardApiLock();
            default:
                return null;
        }
    }


    @Override
    public String getTicket(TicketType ticketType) {
        switch (ticketType) {
            case SDK:
                return getValueFromRedis(SDK_TICKET);
            case JSAPI:
                return  getValueFromRedis(JSAPI_TICKET);
            case WX_CARD:
                return getValueFromRedis(CARD_API_TICKET);
            default:
                return null;
        }
    }


    @Override
    public boolean isTicketExpired(TicketType type) {
        switch (type) {
            case SDK:
                return System.currentTimeMillis() > getExpireFromRedis(SDK_TICKET);
            case JSAPI:
                return System.currentTimeMillis() > getExpireFromRedis(JSAPI_TICKET);
            case WX_CARD:
                return System.currentTimeMillis() > getExpireFromRedis(CARD_API_TICKET);
            default:
                return false;
        }
    }
    @Override
    public void expireTicket(TicketType ticketType) {
        switch (ticketType) {
            case JSAPI:
                setExpire(JSAPI_TICKET, 0);
                break;
            case WX_CARD:
                setExpire(CARD_API_TICKET, 0);
                break;
            case SDK:
                setExpire(SDK_TICKET, 0);
                break;
            default:
        }
    }

    @Override
    public synchronized void updateTicket(TicketType type, String ticket, int expiresInSeconds) {
        switch (type) {
            case JSAPI:
                // 预留200秒的时间
                setValueToRedis(JSAPI_TICKET, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L, ticket);
                break;
            case WX_CARD:
                // 预留200秒的时间
                setValueToRedis(CARD_API_TICKET, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L, ticket);
                break;
            case SDK:
                // 预留200秒的时间
                setValueToRedis(SDK_TICKET, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L, ticket);
                break;
            default:
        }
    }

    @Override
    public String getAppId() {
        return this.appId;
    }

    @Override
    public String getSecret() {
        return this.secret;
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
    public String getTemplateId() {
        return this.templateId;
    }

    @Override
    public long getExpiresTime() {
        return this.expiresTime;
    }

    @Override
    public String getOauth2redirectUri() {
        return this.oauth2redirectUri;
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
    public File getTmpDirFile() {
        return this.tmpDirFile;
    }

    @Override
    public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
        return this.apacheHttpClientBuilder;
    }

    @Override
    public String toString() {
        return WxMpGsonBuilder.create().toJson(this);
    }

    @Override
    public boolean autoRefreshToken() {
        return true;
    }

    @Override
    public WxMpHostConfig getHostConfig() {
        return null;
    }



    /**
     * 获取key的字符串
     *
     * @param key
     * @return
     */
    private String getRedisKey(String key) {
        StringBuilder redisKey = new StringBuilder("mpConfig:");
        if (mpId == null) {
            return redisKey.append(key).toString();
        } else {
            return redisKey.append(mpId).append(":").append(key).toString();
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
