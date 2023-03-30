package com.example.boe.Service.Impl;

import com.example.boe.Config.LoginInterceptorConfig;
import com.example.boe.Entity.User;
import com.example.boe.Entity.UserToken;
import com.example.boe.Form.LoginUser;
import com.example.boe.Form.UserParam;
import com.example.boe.Repository.UserLoginLogRepository;
import com.example.boe.Repository.UserRepository;
import com.example.boe.Repository.UserTokenRepository;
import com.example.boe.Service.LoginService;
import com.example.boe.Util.*;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import com.example.boe.result.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private JwtTokenUtils   jwtTokenUtils;

    @Override
    public ResponseData login(LoginUser loginUser, String from, HttpServletResponse response) {
        String username = null;
        String password = null;
        try {
            username = RSAEncrypt.decrypt(loginUser.getUserName());
            password = RSAEncrypt.decrypt(loginUser.getPassword());
        } catch (Exception e) {
            throw new ServiceException("登陆出错");
        }

        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            throw new ServiceException("用户名密码不为空");
        }
        User user = userRepository.findUserByUsername(username);
        if(user != null && user.getLoginErrorTimes() >= 5 && (System.currentTimeMillis() - user.getLockTime().getTime()) <= 24 * 60 * 60 * 1000) {
            LocalDateTime localDateTime = user.getLockTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
            throw new ServiceException("用户已被锁定；将在"+localDateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"))+"后自动解锁");
        }

        if(user == null || !StringUtils.hasLength(user.getPassword()) || !user.getPassword().equals(PasswordEncoder.encode(password))) {

            String errorMsg = "用户名或密码错误";
            if(user != null) {
                userLoginLogRepository.insertLoginLog(0, username,"用户名或密码错误");
                Integer loginErrorTimes = user.getLoginErrorTimes();


                if(loginErrorTimes == 0) {
                    user.setLockTime(new Timestamp(System.currentTimeMillis()));
                } else if((System.currentTimeMillis() - user.getLockTime().getTime()) >= 5 * 60 * 1000 ) {
                    loginErrorTimes = 0;
                    user.setLockTime(new Timestamp(System.currentTimeMillis()));
                }

                user.setLoginErrorTimes(loginErrorTimes + 1);
                userRepository.save(user);

                if(loginErrorTimes == 4) {
                    errorMsg = "用户已被锁定";
                }
            }

            throw new ServiceException(errorMsg);
        } else { //登录成功则解锁
            user.setLoginErrorTimes(0);
            userRepository.save(user);
        }


        userLoginLogRepository.insertLoginLog(1, username,"登陆成功");

        try {
            String s = PasswordEncoder.encode(user.getId() + "-" + Snowflake.getSnowflakeId());
            Integer userId = user.getId();
            Cookie cookie = new Cookie(jwtTokenUtils.getTokenHeader(), s);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            user.setPassword(null);
            user.setToken(s);
            user.setCreateTime(null);
            user.setUsername(AESCode.encrypt(user.getUsername(), AESCode.USER_NAME_KEY));
            user.setId(null);
            boolean insert = true;
            if(insert) {
                UserToken userToken = new UserToken();
                userToken.setUserId(userId);
                userToken.setLoginFrom(from);
                userToken.setToken(s);
                userToken.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                userTokenRepository.save(userToken);
            }

        } catch (ServiceException exception) {
            throw exception;
        } catch (Exception e) {
            throw new ServiceException("登录出错");
        }

        return new ResponseData(ExceptionMsg.SUCCESS,user);
    }

    @Override
    public ResponseData logout(HttpServletRequest request, HttpServletResponse response) {
        String token = LoginInterceptorConfig.getToken(request, jwtTokenUtils);
        if(!StringUtils.isEmpty(token)) {
           userTokenRepository.deleteByToken(token);
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        return null;
    }

    @Override
    public ResponseData getList(UserParam userParam, User user) {
        if(user.getRoleId()!=1) {
            throw new ServiceException("无权限");
        }
        Pageable pageable = PageRequest.of(userParam.getCurrent(), userParam.getSize());
        Page<User> page =userRepository.findByParam(userParam.getUserName(), userParam.getRole(), pageable);
        List<User> users = page.getContent();

        return new ResponseData(ExceptionMsg.SUCCESS,users);
    }

    public static void main(String[] args) {
        try {
            System.out.println(RSAEncrypt.decrypt("drkXO1d6gQdDILoUIB7kbbfSRnb8/Dsmf3byL2SMBwai0DaGV0DpmBai1FNgofOnrzHYf28yYdWNI48o+8WqLGI/bJy1rY8OR0/KG3f1/rPJU9Lz5Fk3xU+cEK3TTzOOH4fi5Xjf1LpWdPXCGAV1+NNaKkeNCIrXQT6nLx+tYG0="));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
