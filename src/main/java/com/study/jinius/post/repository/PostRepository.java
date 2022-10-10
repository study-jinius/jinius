package com.study.jinius.post.repository;

import com.study.jinius.account.model.Account;
import com.study.jinius.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAccount(Account account);
}
