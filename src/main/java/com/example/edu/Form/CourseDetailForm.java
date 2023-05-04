package com.example.edu.Form;

import com.example.edu.Entity.File;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class CourseDetailForm {
    private String id; //树形结构的id
    private Integer sId;//数据库中的id
    private String name;//课程名
    private Timestamp[] date;//课程时间
    private String label;//课程标签
    private String currency;
    private List<File> fileList;//课程文件
    private float rate;//进度百分比
    private String status;//课程状态 进度条颜色
    private String variableName;//课程状态 文字描述 例如：已完成,进行中,未开始
    private Float variableValue;//课程状态 数字描述 例如：1,2,3
    private String variableUp;//三角形方向
    private List<CourseDetailForm> children;//子节点
}
