package com.example.boe.View;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "stu_cou_view", schema = "edu")
public class StuCouView {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "cla_id")
    private int claId;
    @Column(name = "cou_id")
    private int couId;
    @Column(name = "stu_id")
    private int stuId;
    @Column(name = "teacher_id")
    private int teacherId;
    @Column(name = "class_name")
    private String courseName;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;
}
