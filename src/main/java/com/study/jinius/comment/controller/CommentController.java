package com.study.jinius.comment.controller;

import com.study.jinius.comment.model.*;
import com.study.jinius.comment.service.CommentService;
import com.study.jinius.post.model.PostUpdateParam;
import com.study.jinius.post.model.PostUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "댓글 작성", tags = "CommentController")
    public ResponseEntity<CreateCommentResponse> createComment(@RequestBody CommentCreateRequest request) {
        CommentCreateParam param = request.toParam();
        CreateCommentResponse response = commentService.createComment(param);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{idx}")
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "댓글 수정", tags = "CommentController")
    public ResponseEntity<CommentUpdateResponse> updateComment(@PathVariable Long idx,
                                                               @RequestBody PostUpdateRequest request) {
        PostUpdateParam param = request.toParam();
        CommentUpdateResponse response = commentService.updateComment(idx, param);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")

    @Operation(summary = "댓글 삭제", tags = "CommentController")
    public ResponseEntity<Long> deleteComment(@PathVariable Long idx) {
        Long deletedCommentIdx = commentService.deleteComment(idx);
        return new ResponseEntity<>(deletedCommentIdx, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "게시물 내 댓글 조회", tags = "CommentController")
    public ResponseEntity<List<CommentResponse>> getList(@RequestParam Long postIdx) {
        List<CommentResponse> responseList = commentService.getList(postIdx);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
