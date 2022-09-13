package com.study.jinius.post.controller;

import com.study.jinius.common.model.CommonResponse;
import com.study.jinius.post.model.*;
import com.study.jinius.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public CommonResponse<CreatePostResponse> createPost(@RequestBody CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request.toParam());
        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @PutMapping
    public CommonResponse<UpdatePostResponse> updatePost(@RequestBody UpdatePostRequest request) {
        UpdatePostResponse response = postService.updatePost(request.toParam());
        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @DeleteMapping("/{idx}")
    public CommonResponse<Long> deletePost(@PathVariable Long idx) {
        Long deletedPostIdx = postService.deletePost(idx);
        return new CommonResponse<>(HttpStatus.OK, deletedPostIdx);
    }

    @GetMapping("/list")
    public CommonResponse<List<PostResponse>> getList() {
        List<PostResponse> responseList = postService.getList();
        return new CommonResponse<>(HttpStatus.OK, responseList);
    }
}
