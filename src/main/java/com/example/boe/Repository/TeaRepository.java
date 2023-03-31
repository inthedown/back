package com.example.boe.Repository;

import com.example.boe.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeaRepository extends JpaRepository<Teacher, Integer> {
    @Query("select u from Teacher u where u.userName = ?1 and u.password = ?2")
    Teacher findByUsernameAndPassword(String accountName, String password);

    @Query("select u from Teacher u where u.userName = ?1")
    Teacher findByAccountName(String accountName);
}
