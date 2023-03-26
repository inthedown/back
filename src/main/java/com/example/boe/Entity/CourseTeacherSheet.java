package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cou_tea_sheet", schema = "edu")
public class CourseTeacherSheet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "teacher_id")
    private int teacherId;
}
