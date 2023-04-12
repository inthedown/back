package com.example.edu.Form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CourseFrontDto {
    private Integer id;
    private String courseName;
    private Integer teacherId;
    private String teacherName;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer sessionNum;
}
