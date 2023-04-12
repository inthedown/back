package com.example.edu.Form;

import lombok.Data;

/**
 *  userName: '',
 *       password: '',
 *       name: '',
 *       role: '',  1: 管理员，2:学生，3:教师
 *       email: '',
 *       grade: '',
 *       info: '',
 */

@Data
public class UserDto {
    private String userName;
    private String password;
    private String name;
    private Integer roleId;
    private String email;
    private String grade;
    private String info;
}
