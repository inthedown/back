package com.example.edu.Repository;

import com.example.edu.Entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByChildSessions(Session childSessions);

    List<Session> findByCourseId(Integer courseId);
}
