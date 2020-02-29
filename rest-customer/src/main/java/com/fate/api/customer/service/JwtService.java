package com.fate.api.customer.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fate.api.customer.dto.JwtToken;
import com.fate.api.customer.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 生成验证JWT
 */
@Service
@Slf4j
public class JwtService {
    @Autowired
    CacheService cacheService;

    private static final String ISSUER="wanwuyoulian.com";
    private static final String SUBJECT="token";
    private static final String PREFIX=":ctoken:";
    private static final String SECRETKEY="w2r72hfs2hfor0";
    private static final Long EXPIRE=72000L;//12H


    /**
     * 生成token
     * @param jwtToken
     * @return
     */
    public String createToken(JwtToken jwtToken) {
        String token="";
        String key=getTokenKey(jwtToken);
        token= JWT.create().withKeyId(key)
                .withIssuer(ISSUER)
                .withSubject(SUBJECT)
                .withIssuedAt(new Date())
                .withAudience(JSON.toJSONString(jwtToken))
                .sign(Algorithm.HMAC256(SECRETKEY));
        cacheService.del(key);
        cacheService.set(key,token,EXPIRE);
        return token;
    }

    /**
     * 删除token
     * @param token
     */
    public void abolishToken(String token) {
        String key=JWT.decode(token).getKeyId();
        cacheService.del(key);
    }

    /**
     * 生产唯一的token key
     * @param jwtToken
     * @return
     */
    private String getTokenKey(JwtToken jwtToken){
        return jwtToken.getMerchantId()+PREFIX+jwtToken.getUserId();
    }

    /**
     * 校验JWT
     * @param token
     * @return
     */
    public Optional<JwtToken> verifierToken(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRETKEY)).build();
            String json=jwtVerifier.verify(token).getAudience().get(0);
            if (json!=null){
                String key=JWT.decode(token).getKeyId();
                String cacheToken= (String) cacheService.get(key);
                if (cacheToken!=null && cacheToken.equals(token)){
                    Optional<JwtToken> jwt=Optional.ofNullable(JSONObject.parseObject(json,JwtToken.class));
                    return jwt;
                }
            }
            return Optional.empty();
        }catch (Exception e){
            log.error("校验失败",e);
            throw new AuthException();
        }
    }


    public static void main(String[] args) {
        JwtToken jwtToken=new JwtToken();
        jwtToken.setUserId(11111111L);
        String token= JWT.create().withKeyId(PREFIX+jwtToken.getUserId())
                .withIssuer(ISSUER)
                .withSubject(SUBJECT)
                .withIssuedAt(new Date())
                .withAudience(JSON.toJSONString(jwtToken))
                .sign(Algorithm.HMAC256(SECRETKEY));
        System.out.println(token);
    }
}
