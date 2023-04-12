package com.example.edu.Repository;

import com.example.edu.Entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByChildSessions(Session childSessions);
}
