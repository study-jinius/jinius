package com.study.jinius.post.model;

import com.study.jinius.account.model.Account;
import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateParam {
    private String content;

    private Long accountId;

    private Status status;

    public Post toPost(Account account) {
        return new Post(
                this.status,
                this.content,
                account
        );
    }
}
