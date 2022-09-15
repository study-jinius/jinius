package com.study.jinius.comment.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateCommentResponse {
    private Long idx;
    private String content;
    private Long accountId;
    private Long parentIdx;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Status status;
    private Long postIdx;

    public static UpdateCommentResponse from(Comment comment) {
        UpdateCommentResponse response = new UpdateCommentResponse();
        response.setIdx(comment.getIdx());
        response.setContent(comment.getContent());
        response.setAccountId(comment.getAccountId());
        response.setParentIdx(comment.getParent() != null ? comment.getParent().getIdx() : null);
        response.setCreateDate(comment.getCreateDate());
        response.setUpdateDate(comment.getUpdateDate());
        response.setStatus(comment.getStatus());
        response.setPostIdx(comment.getPost().getIdx());

        return response;
    }
}
