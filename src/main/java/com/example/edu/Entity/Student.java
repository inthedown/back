package com.example.edu.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "student", schema = "edu")
@DiscriminatorValue("student")
@JsonIgnoreProperties(value={"classes"})
public class Student extends User {


    @Basic
    @Column(name = "grade")
    private String grade;

    //q:toString()出现循环引用
    //a:在Student类中加入注解@JsonBackReference
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "classes_id")
    private Classes classes;



    public Student() {

    }

    public Student(String userName, String password, String name ,String email, String grade) {
        super(userName, password, name, email);
        this.grade = grade;

    }
}
