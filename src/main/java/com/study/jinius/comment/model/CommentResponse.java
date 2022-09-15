package com.study.jinius.comment.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long idx;
    private String content;
    private Long accountId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Long parentId;

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setIdx(comment.getIdx());
        response.setContent(comment.getContent());
        response.setAccountId(comment.getAccountId());
        response.setCreateDate(comment.getCreateDate());
        response.setUpdateDate(comment.getUpdateDate());
        response.setParentId(comment.getParent() != null ? comment.getParent().getIdx() : null);

        return response;
    }
}
