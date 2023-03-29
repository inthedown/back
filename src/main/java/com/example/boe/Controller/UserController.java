package com.example.boe.Controller;

import com.example.boe.Form.LoginDto;
import com.example.boe.Form.ToDto;
import com.example.boe.Service.UserService;
import com.example.boe.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    //登录验证
    @PostMapping("/login")
    public ResponseData login(@RequestBody LoginDto loginDto) {

        return userService.login(loginDto);
    }

    //注册
    @PostMapping("/register")
    public ResponseData register(@RequestBody LoginDto loginDto) {

        return userService.register(loginDto);
    }

    //修改
    @PostMapping("/change")
    public ResponseData change(@RequestBody LoginDto loginDto) {

        return userService.change(loginDto);
    }

    //删除
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody LoginDto loginDto) {

        return userService.delete(loginDto);
    }

    @PostMapping("/addUser")
    public ResponseData addUser(@RequestBody LoginDto loginDto) {
        return userService.addUser(loginDto);
    }
    @PostMapping("/stuToCla")
    public ResponseData stuToCla(@RequestBody ToDto toDto) {
        return userService.stuToCla(toDto);
    }
}
