package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoSearchResponse {
    private final String title;
    private final int managerCount;
    private final int totalCommentCount;

    public TodoSearchResponse(String title, int managerCount, int totalCommentCount){
        this.title = title;
        this.managerCount = managerCount;
        this.totalCommentCount = totalCommentCount;
    }
}
