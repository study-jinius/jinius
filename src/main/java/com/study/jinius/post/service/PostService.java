package com.study.jinius.post.service;

import com.study.jinius.common.model.Status;
import com.study.jinius.post.model.*;
import com.study.jinius.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public CreatePostResponse createPost(CreatePostParam param) {
        Post post = param.toPost();
        Post result = postRepository.save(post);

        return CreatePostResponse.from(result);
    }

    public UpdatePostResponse updatePost(Long idx, UpdatePostParam param) {
        Post post = postRepository.findById(idx).orElseThrow();

        post.updatePost(param.getContent(), param.getStatus());
        Post result = postRepository.save(post);

        return UpdatePostResponse.from(result);
    }

    public Long deletePost(Long idx) {
        Post post = postRepository.findById(idx).orElseThrow();
        post.deletePost();
        Post result = postRepository.save(post);

        return result.getIdx();
    }

    public List<PostResponse> getList() {
        List<Post> postList = postRepository.findAll();

        return postList.stream()
                .filter(post -> Status.ACTIVE.equals(post.getStatus()))
                .map(PostResponse::from).toList();
    }
}
