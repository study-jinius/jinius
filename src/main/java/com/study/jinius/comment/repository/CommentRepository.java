package com.study.jinius.comment.repository;

import com.study.jinius.comment.model.Comment;
import com.study.jinius.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
