package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class UpdatePostResponse {
    private Long idx;

    private String content;

    private Long accountId;

    private LocalDateTime updateDate;

    private Status status;

    public static UpdatePostResponse from(Post post) {
        UpdatePostResponse response = new UpdatePostResponse();
        response.setIdx(post.getIdx());
        response.setContent(post.getContent());
        response.setAccountId(post.getAccountId());
        response.setUpdateDate(post.getUpdateDate());
        response.setStatus(post.getStatus());

        return response;
    }
}
