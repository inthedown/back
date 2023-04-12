package com.example.edu.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "user_token", schema = "boe")
public class UserToken {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "token")
    private String token;

    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;

    @Basic
    @Column(name = "user_id")
    private int userId;

    @Basic
    @Column(name = "login_from")
    private String loginFrom;

    @Transient
    private static final long serialVersionUID = 1L;
}
