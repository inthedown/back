package com.example.edu.Form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FileForm {
    private int id;
    private String name;
    private String relativePath;
    private String url;
    private String resolution ;
    private String type ;
    private String size;
    private String uid;
    private String author;
    private Timestamp updateTime;
}
