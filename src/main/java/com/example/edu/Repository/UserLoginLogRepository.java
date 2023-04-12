package com.example.edu.Repository;

import com.example.edu.Entity.UserLoginLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserLoginLogRepository extends PagingAndSortingRepository<UserLoginLog, Integer> {
    @Query(value = "insert into user_login_log(if_success, username, remark,login_time) values(?1, ?2, ?3, now())", nativeQuery = true)
    void insertLoginLog(int i, String username, String remark);
}