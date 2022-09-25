package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCreateParam {
    private String content;

    private Long accountId;

    private Status status;

    public Post toPost() {
        return new Post(
                this.content,
                this.accountId,
                LocalDateTime.now(),
                null,
                this.status);
    }
}
