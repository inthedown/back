package com.example.edu.Service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.edu.Config.LoginInterceptorConfig;
import com.example.edu.Entity.*;
import com.example.edu.Form.ImportDto;
import com.example.edu.Form.LoginUser;
import com.example.edu.Form.UserDto;
import com.example.edu.Form.UserParam;
import com.example.edu.Repository.*;
import com.example.edu.Service.LoginService;
import com.example.edu.Util.*;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import com.example.edu.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private JwtTokenUtils   jwtTokenUtils;

    @Autowired
    private StuRepository stuRepository;
    @Override
    public ResponseData login(LoginUser loginUser, String from, HttpServletResponse response) {
        String username = null;
        String password = null;
        try {
            username = RSAEncrypt.decrypt(loginUser.getUserName());
            password = RSAEncrypt.decrypt(loginUser.getPassword());
        } catch (Exception e) {
           return new ResponseData(ExceptionMsg.FAILED,"用户名密码解密失败");
        }

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return new ResponseData(ExceptionMsg.FAILED,"用户名密码不为空");
        }
        log.info("username:{}",username+"password:{}",password);
        User user = userRepository.findUserByUsername(username);
        if(user != null && user.getLoginErrorTimes().intValue() >= 5 && (System.currentTimeMillis() - user.getLockTime().getTime()) <= 24 * 60 * 60 * 1000) {
            LocalDateTime localDateTime = user.getLockTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
            throw new ServiceException("用户已被锁定；将在"+localDateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"))+"后自动解锁");
        }

        if(user == null ||StringUtils.isBlank(user.getPassword()) || !user.getPassword().equals(PasswordEncoder.encode(password))) {

            userLoginLogRepository.insertLoginLog(0, username,"用户名或密码错误");
            throw new ServiceException("用户名或密码错误");
        } else  if(user != null) {

            Integer loginErrorTimes = user.getLoginErrorTimes().intValue();

            if(!user.getRoleId().equals(loginUser.getRole   ())){
                throw new ServiceException("角色错误");
            }
            if(loginErrorTimes == 0) {
                user.setLockTime(new Timestamp(System.currentTimeMillis()));
            } else if((System.currentTimeMillis() - user.getLockTime().getTime()) >= 5 * 60 * 1000 ) {
                loginErrorTimes = 0;
                user.setLockTime(new Timestamp(System.currentTimeMillis()));
            }

            user.setLoginErrorTimes(loginErrorTimes + 1);
            userRepository.save(user);

            if(loginErrorTimes == 4) {
                throw new ServiceException("用户已被锁定");
            }

        }else{ //登录成功则解锁
            user.setLoginErrorTimes(0);
            userRepository.save(user);
        }

        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setLoginTime(new Timestamp(System.currentTimeMillis()));
        userLoginLog.setIfSuccess("1");
        userLoginLog.setRemark("登陆成功");
        userLoginLog.setUsername(username);

        userLoginLogRepository.save(userLoginLog);

        try {

            String s = PasswordEncoder.encode(user.getId() + "-" + Snowflake.getSnowflakeId());
            Integer userId = user.getId();
            Cookie cookie = new Cookie(jwtTokenUtils.getTokenHeader(), s);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("password", null);
            userMap.put("token", s);
            userMap.put("createTime", null);
            userMap.put("userName", AESCode.encrypt(user.getUserName(), AESCode.USER_NAME_KEY));
            userMap.put("id", null);
                UserToken userToken = new UserToken();
                userToken.setUserId(userId);
                userToken.setLoginFrom(from);
                userToken.setToken(s);
                userToken.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                userTokenRepository.save(userToken);

            return new ResponseData(ExceptionMsg.SUCCESS,userMap);
        } catch (ServiceException exception) {
            throw exception;
        } catch (Exception e) {
            throw new ServiceException("登录出错");
        }


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
//        if(user.getRoleId()!=1) {
//            throw new ServiceException("无权限");
//        }
        int current =0;
        int size = 10;
        String userName = null;
        Integer role = null;
        if (userParam != null) {
            current = userParam.getCurrent()-1;
            size = userParam.getSize();
            userName = StringUtils.isBlank( userParam.getUserName())?null:userParam.getUserName();
            role = StringUtils.isBlank(userParam.getRoleId())?null:Integer.valueOf( userParam.getRoleId());
        }

        Pageable pageable = PageRequest.of(current, size);
        Page<User> page = userRepository.findByParam(userName,role,pageable);
        List<User> list = page.getContent();
        return new ResponseData(ExceptionMsg.SUCCESS,list);
    }

    @Override
    public ResponseData addUser(UserDto userDto,User user1) {
        if(user1.getRoleId()!=1) {
            throw new ServiceException("无权限");
        }

        if(userRepository.findUserByUsername(userDto.getUserName()) != null) {
            throw new ServiceException("用户名已存在");
        }else{
            userDto.setPassword(PasswordEncoder.encode(userDto.getPassword()));
        }
        User user = new User();
        //教师学生继承user,所以这里需要判断
        if(userDto.getRoleId().equals(3)) {
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(userDto,teacher);
            teacher.setCreateTime(new Timestamp(System.currentTimeMillis()));
            userRepository.save(teacher);
        } else if(userDto.getRoleId().equals(2)) {
            Student student = new Student();
            BeanUtils.copyProperties(userDto,student);
            student.setCreateTime(new Timestamp(System.currentTimeMillis()));
            userRepository.save(student);
        } else {
            BeanUtils.copyProperties(userDto,user);
            user.setCreateTime(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }
        return new ResponseData(ExceptionMsg.SUCCESS);
    }

    @Override
    public ResponseData deleteUser(Integer[] ids) {
        //jpa批量删除
        userRepository.deleteByIdIn(Arrays.asList(ids));
        return new ResponseData(ExceptionMsg.SUCCESS);
    }

    @Override
    public ResponseData seePwd(Integer id, User user) {
        if(user.getRoleId()!=1) {
            throw new ServiceException("无权限");
        }
        User user1 = userRepository.findById(id).get();
        return new ResponseData(ExceptionMsg.SUCCESS,PasswordEncoder.decode(user1.getPassword()));

    }

    public static void main(String[] args) {
//        try {
//            System.out.println(RSAEncrypt.decrypt("x/CUPHRBmIrMU7istdwJbi9fEU1vtuq08AybbW0caHqZLaheo9xsOv2lUwH8Vix1fprhjH2cPKdXP6KPLYeIikz2jNrqGOFru6dwCIE7Frrk+PGDq/6lRRnW7bxhJu29AJQA75/f4JVjW+5S3Xoo5OwqQ8wowUt8156oQTubg5o="));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Autowired
    private ClassesRepository classesRepository;
    @Override
    public ResponseData importStu(ImportDto importDto,User user) {
        if(user.getRoleId()!=1) {
            throw new ServiceException("无权限");
        }
        List<Integer> stuIds =importDto.getStuIds();
        if (stuIds == null || stuIds.size() == 0) {
            return new ResponseData(ExceptionMsg.FAILED,"学生不能为空");
        }

        Integer classId = importDto.getClassId();
        if(classId == null) {
            return new ResponseData(ExceptionMsg.FAILED,"班级不能为空");
        }
        List<Student> students = stuRepository.findAllById(stuIds);
        Optional<Classes> classes = Optional.ofNullable( classesRepository.findById(classId)).orElseThrow(() -> new ServiceException("班级不存在"));
        for (Student student : students) {
            student.setClasses(classes.get());
        }
        stuRepository.saveAll(students);
        return new ResponseData(ExceptionMsg.SUCCESS,"导入成功");
    }

    @Override
    public ResponseData getMenus(User user) {
        int roleId = user.getRoleId();

        JSONObject adminMenu=new JSONObject();
        adminMenu.put("name","user");
        JSONArray adminChildren=new JSONArray();
        JSONObject claManMenu=new JSONObject(Map.of("name","classesList"));
        JSONObject couManMenu=new JSONObject(Map.of("name","courseList"));
        JSONObject userManMenu=new JSONObject(Map.of("name","userList"));
        adminChildren.add(claManMenu);
        adminChildren.add(couManMenu);
        adminChildren.add(userManMenu);
        adminMenu.put("children",adminChildren);

        JSONObject stuMenu=new JSONObject();
        stuMenu.put("name","course");
        JSONArray stuChildren=new JSONArray();
        JSONObject stuInfoMenu=new JSONObject(Map.of("name","course"));
        JSONObject stuScoreMenu=new JSONObject(Map.of("name","courseDetail"));
        stuChildren.add(stuInfoMenu);
        stuChildren.add(stuScoreMenu);
        stuMenu.put("children",stuChildren);

        JSONObject teaMenu=new JSONObject();
        teaMenu.put("name","course");
        JSONArray teaChildren=new JSONArray();
        JSONObject teaInfoMenu=new JSONObject(Map.of("name","course"));
        JSONObject teaCMenu=new JSONObject(Map.of("name","courseAdd"));
        JSONObject teaDetailMenu=new JSONObject(Map.of("name","courseDetail"));
        teaChildren.add(teaInfoMenu);
        teaChildren.add(teaCMenu);
        teaChildren.add(teaDetailMenu);
        teaMenu.put("children",teaChildren);

        switch (roleId) {
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,Arrays.asList(adminMenu));
            case 2:
                return new ResponseData(ExceptionMsg.SUCCESS,Arrays.asList(stuMenu));
            case 3:
                return new ResponseData(ExceptionMsg.SUCCESS,Arrays.asList(teaMenu));
            default:
                return new ResponseData(ExceptionMsg.SUCCESS,null);
        }

    }
}

