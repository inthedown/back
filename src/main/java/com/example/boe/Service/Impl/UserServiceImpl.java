package com.example.boe.Service.Impl;


import com.example.boe.Entity.Classes;
import com.example.boe.Entity.Course;
import com.example.boe.Entity.Student;
import com.example.boe.Entity.Teacher;
import com.example.boe.Form.LoginDto;
import com.example.boe.Form.ToDto;
import com.example.boe.Repository.ClassesRepository;
import com.example.boe.Repository.CourseRepository;
import com.example.boe.Repository.StuRepository;
import com.example.boe.Repository.TeaRepository;
import com.example.boe.Service.UserService;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import com.example.boe.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private TeaRepository teaRepository;
    @Autowired
    private StuRepository stuRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ClassesRepository classesRepository;

    public ResponseData login(LoginDto loginDto) {
        if (!validateLoginDto(loginDto)) {
            return new ResponseData(ExceptionMsg.ParamError, "无效的输入");
        }
        // 获取登录信息中的账号名、密码和角色
        String accountName = loginDto.getAccountName();
        String password = loginDto.getPassword();
        String role = loginDto.getRole();
        // 根据角色判断是教师还是学生
        if (role.equals("teacher")) {
            // 根据账号名查找教师
            Teacher teacher = teaRepository.findByAccountName(accountName);
            // 判断教师是否存在
            if (teacher == null) {
                return new ResponseData(ExceptionMsg.FAILED, "账号不存在");
            } else if (!teacher.getPassword().equals(password)) {// 判断密码是否正确
                return new ResponseData(ExceptionMsg.FAILED, "密码错误");
            }
        } else if (role.equals("student")) {
            // 根据账号名查找学生
            Student student = stuRepository.findByAccountName(accountName);

            if (student == null) {// 判断学生是否存在
                return new ResponseData(ExceptionMsg.FAILED, "账号不存在");
            } else if (!student.getPassword().equals(password)) {// 判断密码是否正确
                return new ResponseData(ExceptionMsg.FAILED, "密码错误");
            }
        }
        return new ResponseData(ExceptionMsg.SUCCESS, "登录成功");
    }

    @Override
    public ResponseData register(LoginDto loginDto) {
        if (loginDto.getAccountName() == null || loginDto.getPassword() == null || loginDto.getRole() == null) {
            return new ResponseData(ExceptionMsg.FAILED, "账号密码角色不能为空");
        }
        if (loginDto.getRole().equals("teacher")) {
            if (teaRepository.findByAccountName(loginDto.getAccountName()) != null) {
                return new ResponseData(ExceptionMsg.FAILED, "账号已存在");
            }
            teaRepository.save((Teacher) loginDto.toEntity());
        } else if (loginDto.getRole().equals("student")) {
            if (stuRepository.findByAccountName(loginDto.getAccountName()) != null) {
                return new ResponseData(ExceptionMsg.FAILED, "账号已存在");
            }
            stuRepository.save((Student) loginDto.toEntity());
        }
        return null;
    }

    @Override
    public ResponseData change(LoginDto loginDto) {
        if (!validateLoginDto(loginDto)) {
            return new ResponseData(ExceptionMsg.ParamError, "无效的输入");
        }
        // 获取登录信息中的账号名、旧密码和角色
        String accountName = loginDto.getAccountName();
        String oldPassword = loginDto.getOldPassword();
        String role = loginDto.getRole();

        // 获取用户实体类对象
        Object entity = getUserEntity(accountName, role);

        if (entity == null) {
            return new ResponseData(ExceptionMsg.FAILED, "用户不存在");
        }

        // 验证密码是否正确
        if (!verifyPassword(loginDto.getPassword(), oldPassword)) {
            return new ResponseData(ExceptionMsg.FAILED, "密码错误");
        }

        updateUserInfo(entity, loginDto);

        return new ResponseData(ExceptionMsg.SUCCESS, "修改成功");
    }

    @Override
    public ResponseData delete(LoginDto loginDto) {
        // 获取登录信息中的账号名、旧密码和角色
        String role = loginDto.getRole();
        String accountName = loginDto.getAccountName();
        Object entity = getUserEntity(accountName, role);
        if (entity == null) {
            return new ResponseData(ExceptionMsg.FAILED, "用户不存在");
        } else {
            if (role.equals("teacher")) {
                teaRepository.delete((Teacher) entity);
            } else if (role.equals("student")) {
                stuRepository.delete((Student) entity);
            }
        }


        return new ResponseData(ExceptionMsg.SUCCESS, "删除成功");
    }

    @Override
    public ResponseData addUser(LoginDto loginDto) {
        String role = loginDto.getRole();
        if (role.equals("teacher")) {
            Teacher teacher = Optional.ofNullable(teaRepository.findByAccountName(loginDto.getAccountName()))
                    .orElseThrow(() -> new ServiceException("教师不存在"));
            Course course = courseRepository.findByName(loginDto.getCourseName());
            course.setTeacher(teacher);
            courseRepository.save(course);
        } else if (role.equals("student")) {
            Student student = Optional.ofNullable(stuRepository.findByAccountName(loginDto.getAccountName()))
                    .orElseThrow(() -> new ServiceException("学生不存在"));
            Classes classes = classesRepository.findByClassName(loginDto.getClassesName());
            classes.setStudents(Collections.singletonList(student));
            classesRepository.save(classes);
        }
        return new ResponseData(ExceptionMsg.SUCCESS, "添加成功");
    }

    @Override
    @Transactional
    public ResponseData stuToCla(ToDto toDto) {
        String classesName = toDto.getClassesName();
        Integer[] stuIds = toDto.getStuId();
        Classes classes = Optional.ofNullable(classesRepository.findByClassName(classesName)).orElseThrow(() -> new ServiceException("班级不存在"));
        //遍历数组,根据id查找学生
        for (int i = 0; i < stuIds.length; i++) {
            Student student = Optional.ofNullable(stuRepository.findById(stuIds[i]).get()).orElseThrow(() -> new ServiceException("学生不存在"));
            student.setClasses(classes);
            stuRepository.save(student);
        }
        return new ResponseData(ExceptionMsg.SUCCESS, "添加成功");
    }

    // 获取用户实体类对象
    private Object getUserEntity(String accountName, String role) {
        if (role.equals("teacher")) {
            return teaRepository.findByAccountName(accountName);
        } else if (role.equals("student")) {
            return stuRepository.findByAccountName(accountName);
        } else {
            return null;
        }
    }

    // 验证密码是否正确
    private boolean verifyPassword(String password, String oldPassword) {
        return password != null && !password.isEmpty() && password.equals(oldPassword);
    }

    // 验证登录信息
    private boolean validateLoginDto(LoginDto loginDto) {
        return loginDto != null &&
                !StringUtils.isEmpty(loginDto.getAccountName()) &&
                !StringUtils.isEmpty(loginDto.getOldPassword()) &&
                !StringUtils.isEmpty(loginDto.getPassword()) &&
                !StringUtils.isEmpty(loginDto.getRole());
    }

    // 更新密码和其他信息
    private void updateUserInfo(Object entity, LoginDto loginDto) {
        if (entity instanceof Teacher) {
            Teacher teacher = (Teacher) entity;
            String newPassword = loginDto.getPassword();
            if (verifyPassword(newPassword, teacher.getPassword())) {
                teacher.setPassword(newPassword);
            }
            teacher.setEmail(loginDto.getEmail());
            teacher.setName(loginDto.getName());
            teacher.setInfo(loginDto.getInfo());
            teaRepository.save(teacher);
        } else if (entity instanceof Student) {
            Student student = (Student) entity;
            String newPassword = loginDto.getPassword();
            if (verifyPassword(newPassword, student.getPassword())) {
                student.setPassword(newPassword);
            }
            student.setEmail(loginDto.getEmail());
            student.setName(loginDto.getName());
            student.setGrade(loginDto.getGrade());
            stuRepository.save(student);
        }
    }
}
