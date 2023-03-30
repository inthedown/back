package com.example.boe.Util;

import com.example.boe.Entity.User;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenUtils {
    //过期时间 小时
    @Value("${jwt.expire}")
    private int expire;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.header}")
    private String tokenHeader;

    public String generateToken(User user) throws Exception {
        return JwtHelper.generateToken(user, Algorithm.HMAC256(secretKey), expire);
    }

    public User getStaffInfoFromToken(String token) throws Exception {
        return JwtHelper.getStaffInfoFromToken(token, Algorithm.HMAC256(secretKey));
    }
    public String getTokenHeader() {
        return tokenHeader;
    }

    public String getTokenHead() {
        return tokenHeader;
    }
}
