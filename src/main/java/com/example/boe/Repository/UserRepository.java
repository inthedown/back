package com.example.boe.Repository;

import com.example.boe.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    @Query(value = "select * from user where user_name = ?1", nativeQuery = true)
    User findUserByUsername(String username);
    @Query(value = "select * from user where user_name = ?1 and role = ?2", nativeQuery = true)
    Page<User> findByParam(String userName, String role, Pageable pageable);
}
