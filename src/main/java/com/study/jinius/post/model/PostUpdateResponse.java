package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class PostUpdateResponse {
    private Long idx;

    private String content;

    private Long accountId;

    private LocalDateTime updateDate;

    private Status status;

    public static PostUpdateResponse from(Post post) {
        PostUpdateResponse response = new PostUpdateResponse();
        response.setIdx(post.getIdx());
        response.setContent(post.getContent());
        response.setAccountId(post.getAccount().getIdx());
        response.setUpdateDate(post.getUpdateDate());
        response.setStatus(post.getStatus());

        return response;
    }
}
