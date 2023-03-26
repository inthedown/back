package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
@Entity
@Data
@Table(name = "teacher", schema = "edu")
public class Teacher {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "accountName")
    private String accountName;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "role")
    private String role;
    @Basic
    @Column(name = "info")
    private String info;

    public Teacher(String accountName, String password, String name, String email,String info) {
        this.accountName = accountName;
        this.password = password;
        this.email = email;
        this.role = "teacher";
        this.name = name;
        this.info = info;

    }

    public Teacher() {

    }
}
