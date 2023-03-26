package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cla_stu_sheet", schema = "edu")
public class ClassStudentSheet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Basic
    @Column(name = "cla_id")
    private int classId;
    @Basic
    @Column(name = "stu_id")
    private int studentId;
}
