package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "classes", schema = "edu")
public class Classes {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "class_name")
    private String className;

    @OneToMany(mappedBy = "classes", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy="classes", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Course> courses;

}
