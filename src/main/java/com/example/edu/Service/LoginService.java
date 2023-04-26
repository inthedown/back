package com.example.edu.Service;

import com.example.edu.Entity.User;
import com.example.edu.Form.ImportDto;
import com.example.edu.Form.LoginUser;
import com.example.edu.Form.UserDto;
import com.example.edu.Form.UserParam;
import com.example.edu.result.ResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    ResponseData login(LoginUser loginUser, String from, HttpServletResponse response);

    ResponseData logout(HttpServletRequest request, HttpServletResponse response);

    ResponseData getList(UserParam userParam, User user);

    ResponseData addUser(UserDto userDto,User user);

    ResponseData deleteUser(Integer[] ids);

    ResponseData seePwd(Integer id, User user);

    ResponseData importStu(ImportDto importDto, User user);

    ResponseData getMenus(User user);
}
