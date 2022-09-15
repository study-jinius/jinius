package com.study.jinius.comment.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentParam {
    private String content;

    private Status status;
}
