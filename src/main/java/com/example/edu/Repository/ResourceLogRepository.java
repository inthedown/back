package com.example.edu.Repository;

import com.example.edu.Entity.ResourceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceLogRepository extends JpaRepository<ResourceLog, Integer> {
    @Query(value = "select * from resource_log where user_id=?1 and file_id=?2",nativeQuery = true)
    ResourceLog findByUserIdAndFileId(Integer userId, Integer fileId);
    @Query(value = "select * from resource_log where file_id in ?1 and user_id=?2",nativeQuery = true)
    List<ResourceLog> findResourceLogByFileIdInAndStudentId(List<Integer> fileIds, int studentId);
}