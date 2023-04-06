package com.example.boe.Util;

import com.example.boe.Entity.Session;
import org.hibernate.Hibernate;

import java.lang.reflect.Field;
import java.sql.Timestamp;
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
            for(Field field:o.getClass().getDeclaredFields()){
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


    public static float getRate(Timestamp startTime, Timestamp endTime) {
        long now = System.currentTimeMillis();
        long start = startTime.getTime();
        long end = endTime.getTime();
        if (now < start) {
            return 0;
        } else if (now > end) {
            return 1;
        } else {
            return (float) (now - start) / (end - start);
        }
    }

    public static String getStatus(Timestamp startTime, Timestamp endTime) {
        long now = System.currentTimeMillis();
        long start = startTime.getTime();
        long end = endTime.getTime();
        if (now < start) {
            return "B";
        } else if (now > end) {
            return "W";
        } else {
            return "G";
        }
    }
    public static String getVariableName(Timestamp startTime, Timestamp endTime) {
        long now = System.currentTimeMillis();
        long start = startTime.getTime();
        long end = endTime.getTime();
        if (now < start) {
            return "未开始";
        } else if (now > end) {
            return "已完成";
        } else {
            return "进行中";
        }
    }
}
