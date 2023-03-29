package com.example.boe.Repository;


import com.example.boe.Entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ClassesRepository extends JpaRepository<Classes, Integer> {
    @Query(value = "select * from classes where class_name = ?1", nativeQuery = true)
    Classes findByClassName(String className);
    @Query(value = "select * from classes left join student on classes.id = student.classes_id where classes.id = ?1", nativeQuery = true)
    List<Map<String,Object>> findDetailById(int id);
}
