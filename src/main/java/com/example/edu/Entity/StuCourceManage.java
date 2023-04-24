package com.example.edu.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "stu_couse_manage", schema = "edu")
public class StuCourceManage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private double score;

}
