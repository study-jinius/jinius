package com.study.jinius.account.model;

import com.study.jinius.comment.model.Comment;
import com.study.jinius.common.model.AuthType;
import com.study.jinius.common.model.BaseEntity;
import com.study.jinius.post.model.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @Size(max = 30)
    @Column(unique = true, length = 30)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Size(min = 1, max = 10)
    @Column(nullable = false, length = 10)
    private String name;

    private LocalDateTime lastSignedInDate;

    @Size(max = 15)
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @OneToMany(mappedBy = "account")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "account")
    private List<Post> postList;
    @Column(length = 15)
    @Enumerated(value = EnumType.STRING)
    private AuthType authType;

    @Builder
    public Account(String email,
                   String password,
                   String name,
                   LocalDateTime lastSignedInDate,
                   Role role,
                   List<Comment> commentList,
                   List<Post> postList,
                   AuthType authType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastSignedInDate = lastSignedInDate;
        this.role = role;
        this.commentList = commentList;
        this.postList = postList;
        this.authType = authType;
    }

    public boolean isValidAccount() {
        return this.role != Role.NONE;
    }
}
