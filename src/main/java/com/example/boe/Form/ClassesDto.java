package com.example.boe.Form;

import com.example.boe.Entity.Classes;
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
