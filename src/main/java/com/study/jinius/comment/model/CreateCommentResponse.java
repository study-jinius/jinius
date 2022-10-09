package com.study.jinius.comment.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateCommentResponse {
    private Long idx;
    private String content;
    private Long accountId;
    private Long parentIdx;
    private LocalDateTime createDate;
    private Status status;
    private Long postIdx;

    public static CreateCommentResponse from(Comment comment) {
        CreateCommentResponse response = new CreateCommentResponse();
        response.setIdx(comment.getIdx());
        response.setContent(comment.getContent());
        response.setAccountId(comment.getAccount().getIdx());
        response.setParentIdx(comment.getParent() != null ? comment.getParent().getIdx() : null);
        response.setCreateDate(comment.getCreateDate());
        response.setStatus(comment.getStatus());
        response.setPostIdx(comment.getPost().getIdx());

        return response;
    }
}
