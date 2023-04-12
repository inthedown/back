package com.example.edu.Repository;

import com.example.edu.Entity.ResourceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceLogRepository extends JpaRepository<ResourceLog, Integer> {
}