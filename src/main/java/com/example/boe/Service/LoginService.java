package com.example.boe.Service;

import com.example.boe.Entity.User;
import com.example.boe.Form.LoginUser;
import com.example.boe.Form.UserParam;
import com.example.boe.result.ResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    ResponseData login(LoginUser loginUser, String from, HttpServletResponse response);

    ResponseData logout(HttpServletRequest request, HttpServletResponse response);

    ResponseData getList(UserParam userParam, User user);
}