package com.example.edu.Controller;

import com.example.edu.Entity.User;
import com.example.edu.Form.ImportDto;
import com.example.edu.Form.LoginUser;
import com.example.edu.Form.UserDto;
import com.example.edu.Form.UserParam;
import com.example.edu.Service.LoginService;
import com.example.edu.Util.AESCode;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import com.example.edu.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    public ResponseData getList(@RequestBody @Nullable UserParam userParam) {

        return loginService.getList(userParam,getUser());
    }

    @PostMapping("/addUser")
    public ResponseData addUser(@RequestBody UserDto userDto) {
        return loginService.addUser(userDto);
    }

    @PostMapping("/deleteUser")
    public ResponseData deleteUser(@RequestBody Integer[] ids) {
        return loginService.deleteUser(ids);
    }
    @GetMapping("/seePwd/{id}")
    public ResponseData seePwd(@PathVariable("id") Integer id) {
        return loginService.seePwd(id,getUser());
    }
    @PostMapping("/importStu")
    public ResponseData importStu(@RequestBody @Nullable ImportDto importDto) {

        return loginService.importStu(importDto,getUser());
    }
    @GetMapping("/userinfo")
    public ResponseData userinfo() {
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("name",getUser().getName());
        userMap.put("password", null);
        userMap.put("token", getUser().getToken());
        userMap.put("createTime", null);
        userMap.put("role", getUser().getRoleId());
        userMap.put("userName", AESCode.encrypt(getUser().getUserName(), AESCode.USER_NAME_KEY));
        userMap.put("id", getUser().getId());
        return new ResponseData(ExceptionMsg.SUCCESS,userMap);
    }
    @PostMapping("/1111")
    public ResponseData updatePwd111() {
            throw new ServiceException("token 获取失败");

    }
}
