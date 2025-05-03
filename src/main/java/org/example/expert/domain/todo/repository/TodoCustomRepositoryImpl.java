package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public Page<TodoSearchResponse> findByKeyword(String title, String managerNickname, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        QTodo qTodo = QTodo.todo;
        QManager qManager = QManager.manager;
        QComment qComment = QComment.comment;

        BooleanBuilder builder = new BooleanBuilder();

        //제목 키워드로 검색 -> 일부만 일치해도 가능
        if (title != null && !title.isEmpty()) {
            builder.and(qTodo.title.contains(title));
        }

        //매니저 닉네임으로 검색 -> 일부만 일치해도 검색 가능
        if (managerNickname != null && !managerNickname.isEmpty()) {
            Manager manager = jpaQueryFactory.selectFrom(qManager)
                            .where(qManager.user.nickName.contains(managerNickname))
                                    .fetchOne();
            if (manager != null) {
                builder.and(qManager.user.nickName.contains(managerNickname));
            }
        }

        //LocalDate -> LocalDateTime
        //지정된 기간 사이로 검색
        builder.and(qTodo.modifiedAt.between(startDate.atStartOfDay(), endDate.atStartOfDay()));

        List<TodoSearchResponse> result = jpaQueryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        qTodo.title, qManager.id.count(), qComment.id.count()))
                .from(qTodo)
                .leftJoin(qManager).on(qManager.todo.id.eq(qTodo.id))
                .leftJoin(qComment).on(qComment.todo.id.eq(qTodo.id))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(qTodo.id)
                .orderBy(qTodo.modifiedAt.desc()) //최신순 정렬
                .fetch();

        return new PageImpl<>(result, pageable, result.size()); //10페이지씩
    }


}
