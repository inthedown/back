package com.example.edu.Form;

import lombok.Data;
import org.springframework.lang.Nullable;


/**
 * {"id":"1-3","name":"阿松大","label":"大苏打","date":["2023-03-08T16:00:00.000Z","2023-04-11T16:00:00.000Z"],"fileList":[{"name":"Angel By The Wings.m4a","percentage":0,"status":"ready","size":3904632,"raw":{"uid":1679382228976},"uid":1679382228976},{"name":"Elastic Heart.m4a","percentage":0,"status":"ready","size":3127461,"raw":{"uid":1679382228978},"uid":1679382228978},{"name":"binding lights.m4a","percentage":0,"status":"ready","size":2468765,"raw":{"uid":1679382228979},"uid":1679382228979}]}
 */
@Data
public class CourseDto {

    private String name;
    private String classes;
    private int teacherId;
    @Nullable
    private SessionDto data;


}
