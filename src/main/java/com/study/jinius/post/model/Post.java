package com.study.jinius.post.model;

import com.study.jinius.common.model.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue
    private Long idx;

    private String content;

    private Long accountId;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Post(String content,
                Long accountId,
                LocalDateTime createDate,
                LocalDateTime updateDate,
                Status status) {
        this.content = content;
        this.accountId = accountId;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public void updatePost(String content, Status status) {
        this.content = content;
        this.status = status;
        this.updateDate = LocalDateTime.now();
    }

    public void deletePost() {
        this.status = Status.DELETED;
    }
}
