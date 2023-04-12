package com.example.edu.Form;

import com.example.edu.Entity.Classes;
import lombok.Data;

@Data
public class ClassesDto {
    private int id;
    private String className;

    public Classes toEntity() {
        Classes classes = new Classes();
        classes.setId(id);
        classes.setClassName(className);
        return classes;
    }
}
