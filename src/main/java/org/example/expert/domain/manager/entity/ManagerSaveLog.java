package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "log")
@Getter
@NoArgsConstructor
public class ManagerSaveLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 일정 만든 사람 id
    private User accessor;

    public ManagerSaveLog(User accessor, LocalDateTime requestedAt) {
        this.accessor = accessor;
        this.requestedAt = requestedAt;
    }

}
