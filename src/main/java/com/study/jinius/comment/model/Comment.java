package com.study.jinius.comment.model;

import com.study.jinius.common.model.Status;
import com.study.jinius.post.model.Post;
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
public class Comment {
    @Id
    @GeneratedValue
    private Long idx;

    private String content;

    @Column(nullable = false)
    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "PARENT_IDX")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "POST_IDX")
    private Post post;

    public Comment(String content,
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
        this.updateDate = LocalDateTime.now();
    }

    public void deleteComment() {
        this.status = Status.DELETED;
        childList.forEach(Comment::deleteComment);
    }
}
