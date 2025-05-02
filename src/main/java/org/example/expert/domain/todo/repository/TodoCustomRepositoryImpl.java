package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo qTodo = QTodo.todo;
        QUser qUser = QUser.user;

        Todo result = jpaQueryFactory.selectFrom(qTodo)
                .leftJoin(qTodo.user, qUser).fetchJoin() // Todo.user를 외래키로 매핑 + fetchJoin()으로 n+1 방지
                .where(qTodo.id.eq(todoId))  // 클라이언트가 입력한 todoId랑 같은거 찾기
                .fetchOne();  // 하나의 결과만 반환

        return Optional.ofNullable(result);
    }
}
