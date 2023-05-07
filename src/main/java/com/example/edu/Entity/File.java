package com.example.edu.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "file", schema = "edu")
@JsonIgnoreProperties(value = { "resourceLogs","relative_path"})
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
    @Column(name = "resolution")
    private String resolution ;
    @Basic
    @Column(name = "type")
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @JsonManagedReference
    @OneToMany(mappedBy = "file")
    private List<ResourceLog> resourceLogs;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
