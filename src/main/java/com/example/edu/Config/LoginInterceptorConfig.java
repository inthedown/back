package com.example.edu.Config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.edu.Entity.User;
import com.example.edu.Entity.UserToken;
import com.example.edu.Repository.UserRepository;
import com.example.edu.Repository.UserTokenRepository;
import com.example.edu.Util.BaseContextHandler;
import com.example.edu.Util.JwtTokenUtils;
import com.example.edu.result.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

@Component
@Slf4j
public class LoginInterceptorConfig implements HandlerInterceptor {
    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private static  String[] REQUEST_PASS_URL=new String[]{"login","logout", "test/one"};
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authToken=null;


        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        try {
            jwtTokenUtils= WebApplicationContextUtils.getWebApplicationContext(
                    request.getServletContext()).getBean(JwtTokenUtils.class);

            authToken= getToken(request, jwtTokenUtils);
        } catch (NullPointerException e) {
            throw new TokenException("token 获取为空");
        }catch (Exception e) {
            throw new TokenException("token 获取失败");
        }
        String requestURL=String.valueOf(request.getRequestURL());
        for (String passUrl : REQUEST_PASS_URL) {
            boolean result=requestURL.lastIndexOf(passUrl)>0;

            if(result){
                return true ;
            }
        }


        if(StringUtils.isBlank(authToken)){
            throw new TokenException("用户未登录");
        }
        User user = null;

        List<UserToken> userTokenList =userTokenRepository.selectByToken(authToken);
        if(userTokenList == null || userTokenList.size() == 0 || System.currentTimeMillis() - userTokenList.get(0).getUpdateTime().getTime() > 15 * 60_000) {
            throw new TokenException("token过期");
        }
        UserToken userToken = userTokenList.get(0);
        try{
            user = userRepository.findById(userToken.getUserId()).get();
            user.setPassword(null);

        } catch (Exception e) {

            throw e;
        }




        if(user == null || StringUtils.isBlank(user.getUserName())){
            throw new TokenException("无效的token");
        }

        BaseContextHandler.setUser(user);
        if(System.currentTimeMillis() - userToken.getUpdateTime().getTime() > 5 * 60_000) {
            userToken.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            userTokenRepository.save(userToken);
        }

        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
    }

    public static String getToken(HttpServletRequest request, JwtTokenUtils jwtTokenUtils) {
        String result= request.getHeader(jwtTokenUtils.getTokenHeader());
        if(StringUtils.isEmpty(result)) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null)
                for (Cookie cookie : request.getCookies()) {
                    if(cookie.getName().equals(jwtTokenUtils.getTokenHeader())) {
                        result = cookie.getValue();
                        break;
                    }
                }
        }
        return result;
    }

    public User validate(String token){
        try {
            return jwtTokenUtils.getStaffInfoFromToken(token);
        } catch (TokenExpiredException e){
            log.error("token过期");
            throw new TokenException("token过期");
        }catch (JWTVerificationException e){
            log.error("token不合法");
            throw new TokenException("token不合法");
        } catch (Exception e){
            log.error("验证token出错");
            throw new TokenException("验证token出错");
        }
    }
}
