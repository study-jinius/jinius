package com.study.jinius.post.model;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.model.Role;
import com.study.jinius.comment.model.Comment;
import com.study.jinius.common.model.BaseEntity;
import com.study.jinius.common.model.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    public Post(Status status,
                String content,
                Account account) {
        this.content = content;
        this.account = account;
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

    public void addComment(Comment comment) {
        this.commentList.add(comment);

        if (comment.getPost() != this) {
            comment.setPost(this);
        }
    }

    public boolean exists() {
        return this.status != Status.DELETED;
    }
}
