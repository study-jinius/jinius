package com.study.jinius.post.controller;

import com.study.jinius.common.model.CommonResponse;
import com.study.jinius.post.model.*;
import com.study.jinius.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시물 작성", tags = "PostController")
    @PostMapping
    public CommonResponse<PostCreateResponse> createPost(@RequestBody PostCreateRequest request) {
        PostCreateResponse response = postService.createPost(request.toParam());
        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @Operation(summary = "게시물 수정", tags = "PostController")
    @PutMapping("/{idx}")
    public CommonResponse<PostUpdateResponse> updatePost(@PathVariable Long idx, @RequestBody PostUpdateRequest request) {
        PostUpdateResponse response = postService.updatePost(idx, request.toParam());
        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @Operation(summary = "게시물 삭제", tags = "PostController")
    @DeleteMapping("/{idx}")
    public CommonResponse<Long> deletePost(@PathVariable Long idx) {
        Long deletedPostIdx = postService.deletePost(idx);
        return new CommonResponse<>(HttpStatus.OK, deletedPostIdx);
    }

    @Operation(summary = "게시물 리스트 조회", tags = "PostController")
    @GetMapping("/list")
    public CommonResponse<List<PostResponse>> getList() {
        List<PostResponse> responseList = postService.getList();
        return new CommonResponse<>(HttpStatus.OK, responseList);
    }
}
