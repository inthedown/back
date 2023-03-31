package com.example.boe.Repository;

import com.example.boe.Entity.Student;
import com.example.boe.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StuRepository extends JpaRepository<Student, Integer> {
    @Query("select u from Student u where u.userName = ?1 and u.password = ?2")
    Teacher findByUsernameAndPassword(String accountName, String password);

    @Query("select u from Student u where u.userName = ?1")
    Student findByAccountName(String accountName);
}
