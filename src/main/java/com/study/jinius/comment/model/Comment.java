package com.study.jinius.comment.model;

import com.study.jinius.account.model.Account;
import com.study.jinius.common.model.BaseEntity;
import com.study.jinius.common.model.Status;
import com.study.jinius.post.model.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "PARENT_IDX")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "POST_IDX")
    private Post post;

    public Comment(Account account,
                   String content,
                   Status status) {
        this.account = account;
        this.content = content;
        this.status = status;
    }

    public void setPost(Post post) {
        this.post = post;

        if (!post.getCommentList().contains(this)) {
            post.getCommentList().add(this);
        }
    }

    public void setParent(Comment comment) {
        this.parent = comment;

        if (!comment.getChildList().contains(this)) {
            comment.getChildList().add(this);
        }
    }

    public void updateComment(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    public void deleteComment() {
        this.status = Status.DELETED;
        childList.forEach(Comment::deleteComment);
    }

    public boolean exists() {
        return this.status != Status.DELETED;
    }
}
