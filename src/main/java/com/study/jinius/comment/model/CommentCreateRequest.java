package com.study.jinius.comment.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private String content;

    private Long accountId;

    private Long parentId;

    private Long postId;

    private Status status;

    public CommentCreateParam toParam() {
        CommentCreateParam param = new CommentCreateParam();
        param.setContent(this.content);
        param.setAccountId(this.accountId);
        param.setParentId(this.parentId);
        param.setPostId(this.postId);
        param.setStatus(this.status);

        return param;
    }
}
