package com.example.edu.Service.Impl;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
            throw new ServiceException("登陆出错");
        }

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new ServiceException("用户名密码不为空");
        }
        log.info("username:{}",username+"password:{}",password);
        User user = userRepository.findUserByUsername(username);
        if(user != null && user.getLoginErrorTimes().intValue() >= 5 && (System.currentTimeMillis() - user.getLockTime().getTime()) <= 24 * 60 * 60 * 1000) {
            LocalDateTime localDateTime = user.getLockTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
            throw new ServiceException("用户已被锁定；将在"+localDateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"))+"后自动解锁");
        }

        if(user == null ||StringUtils.isBlank(user.getPassword()) || !user.getPassword().equals(PasswordEncoder.encode(password))) {

            String errorMsg = "用户名或密码错误";
            if(user != null) {
                userLoginLogRepository.insertLoginLog(0, username,"用户名或密码错误");
                Integer loginErrorTimes = user.getLoginErrorTimes().intValue();

                if(!user.getRoleId().equals(loginUser.getRoleId())){
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
            user.setUserName(AESCode.encrypt(user.getUserName(), AESCode.USER_NAME_KEY));
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
    public ResponseData addUser(UserDto userDto) {
//        if(userDto.getRole().equals(1)) {
//            throw new ServiceException("无权限");
//        }
        log.info("userDto:{}",userDto);
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
//        if(user.getRoleId()!=1) {
//            throw new ServiceException("无权限");
//        }
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
//        if(user.getRoleId()!=1) {
//            throw new ServiceException("无权限");
//        }
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
}
