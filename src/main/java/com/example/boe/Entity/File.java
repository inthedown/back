package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "file", schema = "boe")
public class File {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "relative_path")
    private String relativePath;
    @Basic
    @Column(name = "url")
    private String url;
    @Basic
    @Column(name = "resolution ")
    private String resolution ;
    @Basic
    @Column(name = "type ")
    private String type ;
    @Basic
    @Column(name = "size")
    private String size;
    @Basic
    @Column(name = "uid")
    private String uid;
    @Basic
    @Column(name = "author")
    private String author;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
