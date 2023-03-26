package com.example.boe.Repository;


import com.example.boe.Entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassesRepository extends JpaRepository<Classes, Integer> {
    @Query(value = "select * from classes where class_name = ?1", nativeQuery = true)
    Classes findByClassName(String className);
}
