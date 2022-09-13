package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long idx;

    private String content;

    private Long accountId;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Status status;
}
