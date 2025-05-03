package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoSearchResponse {
    private final String title;
    private final long managerCount;
    private final long totalCommentCount; //count()는 기본적으로 BIGINT()타입이니까 long 타입이 반환되므로!!

    public TodoSearchResponse(String title, long managerCount, long totalCommentCount){
        this.title = title;
        this.managerCount = managerCount;
        this.totalCommentCount = totalCommentCount;
    }
}
