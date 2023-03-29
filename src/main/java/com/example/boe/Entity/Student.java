package com.example.boe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "student", schema = "edu")
@JsonIgnoreProperties(value={"classes"})
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

    //q:toString()出现循环引用
    //a:在Student类中加入注解@JsonBackReference
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "classes_id")
    private Classes classes;


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
