package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdatePostParam {
    private String content;

    private Long accountId;

    private Status status;
}
