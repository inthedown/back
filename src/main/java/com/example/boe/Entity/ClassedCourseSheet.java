package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cla_cou_sheet", schema = "edu")
public class ClassedCourseSheet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "cla_id")
    private int classId;
    @Basic
    @Column(name = "cou_id")
    private int courseId;
}
