package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateParam {
    private String content;

    private Status status;
}
