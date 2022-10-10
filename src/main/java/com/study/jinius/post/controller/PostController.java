package com.study.jinius.post.controller;

import com.study.jinius.post.model.*;
import com.study.jinius.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "게시물 작성", tags = "PostController")
    public ResponseEntity<PostCreateResponse> createPost(@RequestBody PostCreateRequest request) {
        PostCreateResponse response = postService.createPost(request.toParam());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{idx}")
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "게시물 수정", tags = "PostController")
    public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable Long idx, @RequestBody PostUpdateRequest request) {
        PostUpdateResponse response = postService.updatePost(idx, request.toParam());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "게시물 삭제", tags = "PostController")
    public ResponseEntity<Long> deletePost(@PathVariable Long idx) {
        Long deletedPostIdx = postService.deletePost(idx);
        return new ResponseEntity<>(deletedPostIdx, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "본인이 작성한 게시물 리스트 조회", tags = "PostController")
    public ResponseEntity<List<PostResponse>> getList() {
        List<PostResponse> responseList = postService.getList();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
