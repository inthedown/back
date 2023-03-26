package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "student", schema = "edu")
public class Student {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "account_name")
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
    @Column(name = "grade")
    private String grade;

    public Student(String accountName, String password, String email, String grade, String name) {
        this.accountName = accountName;
        this.password = password;
        this.email = email;
        this.role = "student";
        this.grade = grade;
        this.name = name;
    }

    public Student() {

    }
}
