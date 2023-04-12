package com.example.edu.Form;

import com.example.edu.Entity.Student;
import com.example.edu.Entity.Teacher;
import lombok.Data;

@Data
public class LoginDto {
    private int id;
    private String accountName;
    private String password;
    private String role;
    private String email;
    private String grade;
    private String name;
    private String info;
    private String oldPassword;
    private String classesName;
    private String courseName;
    //根据role转换为对应的实体类
    public Object toEntity() {
        if (role.equals("teacher")) {
            return new Teacher(accountName, password,name, email,  info);
        } else if (role.equals("student")) {
            return new Student(accountName, password, name,email, grade);
        }  else {
            return null;
        }
    }
}
