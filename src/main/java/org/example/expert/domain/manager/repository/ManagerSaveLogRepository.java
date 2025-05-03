package org.example.expert.domain.manager.repository;

import jakarta.transaction.Transactional;
import org.example.expert.domain.manager.entity.ManagerSaveLog;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface ManagerSaveLogRepository extends JpaRepository<ManagerSaveLog, Long> {
}
