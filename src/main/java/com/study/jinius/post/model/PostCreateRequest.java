package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;

@Getter
public class PostCreateRequest {
    private String content;

    private Long accountId;

    private Status status;

    public PostCreateParam toParam() {
        PostCreateParam param = new PostCreateParam();
        param.setContent(this.content);
        param.setAccountId(this.accountId);
        param.setStatus(this.status);

        return param;
    }
}
