package com.example.edu.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "resource_log", schema = "edu")
@JsonIgnoreProperties(value = {"file", "user"})
public class ResourceLog {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Basic
    @Column(name = "time")
    private Timestamp time;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "percent")
    private float percent;
}
