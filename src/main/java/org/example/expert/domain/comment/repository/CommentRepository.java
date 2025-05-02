package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //기존 N+1이 생기는 코드
    //@Query("SELECT c FROM Comment c JOIN c.user WHERE c.todo.id = :todoId")

    //1.  @EntityGraph + @Query로 개선한 버전
    //@EntityGraph(attributePaths = {"user"})
    //@Query("SELECT c FROM Comment c WHERE c.todo.id = :todoId")

    //2. Fetch Join으로 개선한 버전
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.todo.id = :todoId")
    List<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
