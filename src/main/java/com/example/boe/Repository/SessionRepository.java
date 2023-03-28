package com.example.boe.Repository;

import com.example.boe.Entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByChildSessions(Session childSessions);
}
