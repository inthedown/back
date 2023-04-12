package com.example.edu.Repository;

import com.example.edu.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<File,Integer> {
    @Query(value = "select * from file where uid = ?1",nativeQuery = true)
    File findFileByUid(String uid);
}
