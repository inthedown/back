package com.example.edu.Repository;

import com.example.edu.Entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Integer> {
    @Query(value = "insert into user_login_log(if_success, username, remark,login_time) values(?1, ?2, ?3, now())", nativeQuery = true)
    void insertLoginLog(int i, String username, String remark);

    @Query(value = "select u.loginTime from UserLoginLog u where u.username in :usernames and u.loginTime >= :startDate order by u.loginTime desc ")
    List<Timestamp> findLoginTimeByUsernamesAndDateAfter(@Param("usernames")List<String> usernames, @Param("startDate") Timestamp startDate);

}