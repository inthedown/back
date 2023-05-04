package com.example.edu.Repository;

import com.example.edu.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select count(s) from Student s " +
            "where s.classes in (select c.classes from Course c where c.teacher.id=?1)")
    Integer findStuNumByTeaId(Integer id);
    @Query("select s from Student s " +
            "where s.classes in (select c.classes from Course c where c.teacher.id=?1)")
    List<Student> findStuByTeaId(Integer id);
}