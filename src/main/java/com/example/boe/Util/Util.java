package com.example.boe.Util;

import com.example.boe.Entity.Classes;
import com.example.boe.Entity.Session;
import org.hibernate.Hibernate;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public  class Util {
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

    //嵌套对象持久化加载所有懒加载属性
    public static void initial(Object o){
        if(o instanceof Collection){
            for(Object obj:(Collection)o){
                initial(obj);
            }
        }else if(o instanceof Session){
            Session session = (Session)o;
            Hibernate.initialize(session.getFiles());
            Hibernate.initialize(session.getChildSessions());
            initial(session.getChildSessions());
        }else if(o instanceof Object){
            //利用反射获取所有List属性
            for(java.lang.reflect.Field field:o.getClass().getDeclaredFields()){
                if(field.getType().equals(List.class)){
                    try {
                        field.setAccessible(true);
                        List list = (List)field.get(o);
                        initial(list);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
