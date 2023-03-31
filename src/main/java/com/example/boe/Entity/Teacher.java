package com.example.boe.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "teacher", schema = "edu")
@DiscriminatorValue("teacher")
@JsonIgnoreProperties(value={"courses"})
public class Teacher extends User {

    @Basic
    @Column(name = "info")
    private String info;

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Course> courses;



    public Teacher() {
        super();

    }

    public Teacher(String accountName, String password, String name, String email, String info) {
        super(accountName, password, name, email);
        this.info = info;
    }
}
