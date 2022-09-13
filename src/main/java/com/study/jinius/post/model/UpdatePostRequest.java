package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;

@Getter
public class UpdatePostRequest {
    private Long idx;

    private String content;

    private Long accountId;

    private Status status;

    public UpdatePostParam toParam() {
        UpdatePostParam param = new UpdatePostParam();
        param.setIdx(this.idx);
        param.setContent(this.content);
        param.setAccountId(this.accountId);
        param.setStatus(this.status);

        return param;
    }
}
