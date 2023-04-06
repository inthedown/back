package com.example.boe.Repository;

import com.example.boe.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
    @Query(value = "select * from comment where s_id = ?1 order by time desc", nativeQuery = true)
    List<Comment> findAllBySId(int sId);

}