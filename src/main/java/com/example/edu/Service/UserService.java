package com.example.edu.Service;

import com.example.edu.Form.LoginDto;
import com.example.edu.Form.ToDto;
import com.example.edu.result.ResponseData;

public interface UserService {

    //登录
    ResponseData login(LoginDto loginDto);
    //注册
    ResponseData register(LoginDto loginDto);
    //修改
    ResponseData change(LoginDto loginDto);
    //删除
    ResponseData delete(LoginDto loginDto);

    ResponseData addUser(LoginDto loginDto);

    ResponseData stuToCla(ToDto toDto);
}
