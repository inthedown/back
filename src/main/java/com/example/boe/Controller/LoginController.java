package com.example.boe.Controller;

import com.example.boe.Entity.User;
import com.example.boe.Form.LoginUser;
import com.example.boe.Form.UserParam;
import com.example.boe.Service.LoginService;
import com.example.boe.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user1")
public class LoginController extends BaseController{
    @Autowired
    private LoginService loginService;
    @PostMapping("login")
    public ResponseData login(
            @Validated @RequestBody LoginUser loginUser, HttpServletRequest request, HttpServletResponse response
    ) {
        return loginService.login(loginUser, request.getHeader("from"), response);
    }

    @PostMapping("logout")
    public ResponseData logout(HttpServletRequest request, HttpServletResponse response) {
        return loginService.logout(request, response);
    }

    @GetMapping("user")
    public ResponseData user() {
        User user = getUser();
        user.setPassword(null);
        user.setId(null);
        user.setCreateTime(null);
        return new ResponseData(user);
    }


    @PostMapping("/getList")
    public ResponseData getList(@RequestBody UserParam userParam) {

        return loginService.getList(userParam,getUser());
    }

}
