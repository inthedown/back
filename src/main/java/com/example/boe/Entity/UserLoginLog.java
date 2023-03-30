package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "user_login_log", schema = "boe")
public class UserLoginLog {
    //if_success, username, remark
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "if_success")
    private String ifSuccess;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "remark")
    private String remark;
    @Basic
    @Column(name = "login_time")
    private Timestamp loginTime;
}
