package com.example.boe.Repository;

import com.example.boe.Entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserTokenRepository extends JpaRepository<UserToken,Integer> {
    @Query(value = "select * from user_token where token = ?1",nativeQuery = true)
    List<UserToken> selectByToken(String authToken);
    @Query(value = "delete from user_token where token = ?1",nativeQuery = true)
    void deleteByToken(String token);
}