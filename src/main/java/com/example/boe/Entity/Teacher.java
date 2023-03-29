package com.example.boe.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "teacher", schema = "edu")
@JsonIgnoreProperties(value={"courses"})
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

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Course> courses;

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
