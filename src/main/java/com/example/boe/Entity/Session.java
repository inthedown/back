package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "session", schema = "edu")
public class Session {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "session_name")
    private String sessionName;

    @Basic
    @Column(name = "start_time")
    private Timestamp startTime;
    @Basic
    @Column(name = "end_time")
    private Timestamp endTime;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<File> files;

    @OneToMany(mappedBy = "parentSession", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Session> childSessions;

    @ManyToOne
    @JoinColumn(name = "parent_session_id")
    private Session parentSession;



}
