package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user", schema = "edu")
public class User {
    /**
     *
     *
     * @mbg.generated Tue Mar 02 11:52:41 CST 2021
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "user_name")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "email")
    private String email;
    /**
     * 1: 管理员，2:普通用户
     *
     * @mbg.generated Tue Mar 02 11:52:41 CST 2021
     */
    private Integer roleId;

    @Basic
    @Column(name = "user_login_auth")
    private Integer userLoginAuth;

    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

    @Transient
    private String token;

    @Basic
    @Column(name = "login_error_times")
    private Integer loginErrorTimes;
    @Basic
    @Column(name = "lock_time")
    private Timestamp lockTime;


    private static final long serialVersionUID = 1L;

    public User(String accountName, String password, String name, String email) {
        this.username = accountName;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User() {

    }
}