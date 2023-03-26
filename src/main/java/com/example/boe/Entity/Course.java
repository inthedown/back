package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "course", schema = "edu")
public class Course {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "course_name")
    private String courseName;
    @Basic
    @Column(name = "teacher_id")
    private int teacherId;
    @Basic
    @Column(name = "start_time")
    private Timestamp startTime;
    @Basic
    @Column(name = "end_time")
    private Timestamp endTime;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Session> sessions;

    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", teacherId=" + teacherId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
