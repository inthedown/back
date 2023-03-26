package com.example.boe.Util;

import com.example.boe.Entity.Classes;

import java.lang.reflect.Field;

public class Util {
    public static <T, S> void map(T dto, S entity) throws IllegalAccessException {
        Class<?> dtoClass = dto.getClass();
        Class<?> entityClass = entity.getClass();

        Field[] dtoFields = dtoClass.getDeclaredFields();
        for (Field dtoField : dtoFields) {
            dtoField.setAccessible(true);
            Field entityField;
            try {
                entityField = entityClass.getDeclaredField(dtoField.getName());
            } catch (NoSuchFieldException e) {
                // 如果Entity类中不存在DTO类中的属性，则忽略该属性
                continue;
            }
            entityField.setAccessible(true);
            entityField.set(entity, dtoField.get(dto));
        }
    }

    public static void main(String[] args) {
        Classes a=new Classes();
        a.setClassName("a");
        a.setId(1);
        Classes b=new Classes();
        try {
            map(a,b);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(b.toString());
    }
}
