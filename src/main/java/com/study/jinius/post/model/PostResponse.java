package com.study.jinius.post.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {
    private Long idx;
    private String content;
    private Long accountId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static PostResponse from(Post post) {
        PostResponse response = new PostResponse();
        response.setIdx(post.getIdx());
        response.setContent(post.getContent());
        response.setAccountId(post.getAccount().getIdx());
        response.setCreateDate(post.getCreateDate());
        response.setUpdateDate(post.getUpdateDate());

        return response;
    }
}
