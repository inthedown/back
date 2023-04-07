package com.example.boe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@JsonIgnoreProperties(value = {"commentsFrom", "commentsTo", "resourceLogs"})
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
    private String userName;

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

    @JsonBackReference
    @OneToMany(mappedBy = "userFrom", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> commentsFrom;

    @JsonBackReference
    @OneToMany(mappedBy = "userTo", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> commentsTo;


    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<ResourceLog> resourceLogs;
    private static final long serialVersionUID = 1L;




    public User(String accountName, String password, String name, String email) {
        this.userName = accountName;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User() {

    }
}
