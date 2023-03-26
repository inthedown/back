package com.example.boe.Service;

import com.example.boe.Form.LoginDto;
import com.example.boe.result.ResponseData;

public interface UserService {

    //登录
    ResponseData login(LoginDto loginDto);
    //注册
    ResponseData register(LoginDto loginDto);
    //修改
    ResponseData change(LoginDto loginDto);
    //删除
    ResponseData delete(LoginDto loginDto);

}
