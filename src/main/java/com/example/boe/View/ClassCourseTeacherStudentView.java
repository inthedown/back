package com.example.boe.View;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * select `ccs`.`id`        AS `class_course_sheet_id`,
 *        `cs`.`id`         AS `class_id`,
 *        `cs`.`class_name` AS `class_name`,
 *        `cts`.`id`        AS `course_teacher_sheet_id`,
 *        `c`.`id`          AS `course_id`,
 *        `c`.`course_name` AS `course_name`,
 *        `c`.`start_time`  AS `start_time`,
 *        `c`.`end_time`    AS `end_time`,
 *        `t`.`id`          AS `teacher_id`,
 *        `t`.`name`        AS `teacher_name`,
 *        `t`.`email`       AS `teacher_email`,
 *        `css`.`id`        AS `class_student_sheet_id`,
 *        `s`.`id`          AS `student_id`,
 *        `s`.`name`        AS `student_name`,
 *        `s`.`email`       AS `student_email`,
 *        `s`.`grade`       AS `student_grade`
 * from ((((((`edu`.`cla_cou_sheet` `ccs` join `edu`.`classes` `cs` on ((`ccs`.`cla_id` = `cs`.`id`))) join `edu`.`cou_tea_sheet` `cts` on ((`ccs`.`cou_id` = `cts`.`course_id`))) join `edu`.`course` `c` on ((`cts`.`course_id` = `c`.`id`))) join `edu`.`teacher` `t` on ((`c`.`teacher_id` = `t`.`id`))) join `edu`.`cla_stu_sheet` `css` on ((`cs`.`id` = `css`.`cla_id`)))
 *          join `edu`.`student` `s` on ((`css`.`stu_id` = `s`.`id`)));
 */
@Entity(name = "class_course_teacher_student")
@Data
public class ClassCourseTeacherStudentView {
    @Id
    @Column(name = "class_course_sheet_id")
    private int id;
    @Column(name = "class_id")
    private int classId;
    @Column(name = "class_name")
    private String className;
    @Column(name = "course_id")
    private int courseId;
    @Column(name = "course_name")
    private String courseName;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;
    @Column(name = "teacher_id")
    private int teacherId;
    @Column(name = "teacher_name")
    private String teacherName;
    @Column(name = "teacher_email")
private String teacherEmail;
    @Column(name = "student_id")
    private int studentId;
    @Column(name = "student_name")
    private String studentName;
    @Column(name = "student_email")
    private String studentEmail;
    @Column(name = "student_grade")
    private int studentGrade;

}
