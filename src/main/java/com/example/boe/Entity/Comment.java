package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comment", schema = "edu")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Basic
    @Column(name = "content")
    private String content;

    // 提出用户
    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private User userFrom;

    // 接收用户
    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private User userTo;

    @Basic
    @Column(name = "time")
    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;



}
