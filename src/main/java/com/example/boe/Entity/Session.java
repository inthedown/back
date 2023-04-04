package com.example.boe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"parentSession"})
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonManagedReference
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<File> files;

    @JsonManagedReference
    @OneToMany(mappedBy = "parentSession", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Session> childSessions;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_session_id")
    private Session parentSession;

    @JsonManagedReference
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> comments;


}
