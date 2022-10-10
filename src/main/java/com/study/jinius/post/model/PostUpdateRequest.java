package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;

@Getter
public class PostUpdateRequest {
    private String content;
    private Status status;

    public PostUpdateParam toParam() {
        PostUpdateParam param = new PostUpdateParam();
        param.setContent(this.content);
        param.setStatus(this.status);

        return param;
    }
}
