package org.example.expert.domain.todo.repository;

import org.example.expert.client.WeatherClient;
import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.user u " +
            "WHERE DATE_FORMAT(t.modifiedAt, '%Y-%m-%d') " +
            "BETWEEN :startDate AND :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable, String startDate, String endDate);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.user u " +
            "WHERE t.weather = :weather AND DATE_FORMAT(t.modifiedAt, '%Y-%m-%d') " +
            "BETWEEN :startDate AND :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByWeatherContainsOrderByModifiedAtDesc(Pageable pageable, String weather, String startDate, String endDate);

//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN t.user " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
