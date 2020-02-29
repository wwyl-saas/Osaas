package com.fate.api.customer;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fate.api.customer.dto.JwtToken;

import java.util.Date;

/**
 * @program: rest-customer
 * @author: xudongdong
 * @create: 2019-05-27 00:34
 **/
public class TestToken {
    public static void main(String[] args) {
        JwtToken token=new JwtToken(1L,1111L);
        String key="TOKEN_"+token.getUserId();
        String result= JWT.create().withKeyId(key)
                .withIssuer("quark.com")
                .withSubject("scholar")
                .withIssuedAt(new Date())
                .withAudience(JSON.toJSONString(token))
                .sign(Algorithm.HMAC256("my_secret"));
        System.out.println(result);
    }
}
