package com.example.edu.Repository;

import com.example.edu.Entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Integer> {
    @Query(value = "insert into user_login_log(if_success, username, remark,login_time) values(?1, ?2, ?3, now())", nativeQuery = true)
    void insertLoginLog(int i, String username, String remark);
}