package com.study.jinius.comment.model;

import com.study.jinius.account.model.Account;
import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateParam {
    private String content;

    private Long parentId;

    private Long postId;

    private Status status;

    public Comment toComment(Account account) {
        return new Comment(
                account,
                this.content,
                this.status);
    }
}
