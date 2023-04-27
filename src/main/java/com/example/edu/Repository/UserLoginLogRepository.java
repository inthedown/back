package com.example.edu.Repository;

import com.example.edu.Entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Integer> {
    @Query(value = "insert into user_login_log(if_success, username, remark,login_time) values(?1, ?2, ?3, now())", nativeQuery = true)
    void insertLoginLog(int i, String username, String remark);

    @Query(value = "select u.loginTime from UserLoginLog u where u.username in ?1")
    List<Map<String, Object>> findByName(List<String> studentList);
}