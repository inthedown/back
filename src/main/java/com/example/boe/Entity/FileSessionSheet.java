package com.example.boe.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "file_session_sheet", schema = "edu")
public class FileSessionSheet {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "file_id")
    private int fileId;
    @Basic
    @Column(name = "ses_id")
    private int sessionId;
}
