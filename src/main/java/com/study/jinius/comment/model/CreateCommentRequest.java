package com.study.jinius.comment.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;

@Getter
public class CreateCommentRequest {
    private String content;

    private Long accountId;

    private Long parentId;

    private Long postId;

    private Status status;

    public CreateCommentParam toParam() {
        CreateCommentParam param = new CreateCommentParam();
        param.setContent(this.content);
        param.setAccountId(this.accountId);
        param.setParentId(this.parentId);
        param.setPostId(this.postId);
        param.setStatus(this.status);

        return param;
    }
}
