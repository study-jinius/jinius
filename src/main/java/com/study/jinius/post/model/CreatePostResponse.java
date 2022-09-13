package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostResponse {
    private Long idx;

    private String content;

    private Long accountId;

    private LocalDateTime createDate;

    private Status status;

    public static CreatePostResponse from(Post post) {
        CreatePostResponse response = new CreatePostResponse();
        response.setIdx(post.getIdx());
        response.setContent(post.getContent());
        response.setAccountId(post.getAccountId());
        response.setCreateDate(post.getCreateDate());
        response.setStatus(post.getStatus());

        return response;
    }
}
