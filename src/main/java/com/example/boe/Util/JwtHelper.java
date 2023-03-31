package com.example.boe.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.boe.Entity.User;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JwtHelper {

    /**
     * 密钥加密token
     *
     //     * @param jwtStaffInfo 员工信息
     * @param algorithm    加密算法
     * @param expire       过期时间/小时
     * @return
     * @throws Exception
     */
    public static String generateToken(User user, Algorithm algorithm, int expire) throws Exception {

        Calendar passTime=Calendar.getInstance();
        Calendar now=Calendar.getInstance();
        //过期时间
        passTime.add(Calendar.HOUR, expire);
        System.out.println("passTime:"+passTime);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)
                .withClaim("id", user.getId())
                .withClaim("roleId", user.getRoleId())
                .withClaim("userName", user.getUserName())
                .withClaim("name", user.getName())
                .withClaim("createTime", user.getCreateTime())
                .withIssuedAt(now.getTime())  //iat
                .withExpiresAt(passTime.getTime())  //exp
                .sign(algorithm);
        return token;
    }

    /**
     * 公钥解析token
     *
     * @param token
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static DecodedJWT parserToken(String token, Algorithm algorithm) throws Exception{

        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }
    /**
     * 获取token中的员工信息
     *
     * @param token
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static User  getStaffInfoFromToken(String token, Algorithm algorithm) throws Exception{
        DecodedJWT decodedJWT = parserToken(token, algorithm);
//        User user=  JSONArray.parseObject(decodedJWT.getClaim("user").asString(), User.class);
        User user = new User();
        user.setId(decodedJWT.getClaim("id").asInt());
        user.setName(decodedJWT.getClaim("name").asString());
        user.setUserName(decodedJWT.getClaim("username").asString());
        user.setCreateTime(new Timestamp(decodedJWT.getClaim("createTime").asDate().getTime()) );
        user.setRoleId(decodedJWT.getClaim("roleId").asInt());
        return user;
    }
}
