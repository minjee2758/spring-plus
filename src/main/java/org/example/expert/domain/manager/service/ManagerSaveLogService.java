package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.ManagerSaveLog;
import org.example.expert.domain.manager.repository.ManagerSaveLogRepository;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ManagerSaveLogService {
    private final ManagerSaveLogRepository managerSaveLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //부모의 트랜잭션과는 독립적으로 동작함 -> 실패하더라도 로그는 저장
    public void save(User accessor){
        managerSaveLogRepository.save(new ManagerSaveLog(accessor, LocalDateTime.now()));
    }

}
