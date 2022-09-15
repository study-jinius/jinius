package com.study.jinius.comment.controller;

import com.study.jinius.comment.model.*;
import com.study.jinius.comment.service.CommentService;
import com.study.jinius.common.model.CommonResponse;
import com.study.jinius.post.model.PostResponse;
import com.study.jinius.post.model.UpdatePostParam;
import com.study.jinius.post.model.UpdatePostRequest;
import com.study.jinius.post.model.UpdatePostResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성", tags = "CommentController")
    @PostMapping
    public CommonResponse<CreateCommentResponse> createComment(@RequestBody CreateCommentRequest request) {
        CreateCommentParam param = request.toParam();
        CreateCommentResponse response = commentService.createComment(param);

        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @Operation(summary = "댓글 수정", tags = "CommentController")
    @PutMapping("/{idx}")
    public CommonResponse<UpdateCommentResponse> updateComment(@PathVariable Long idx,
                                                               @RequestBody UpdatePostRequest request) {
        UpdatePostParam param = request.toParam();
        UpdateCommentResponse response = commentService.updateComment(idx, param);

        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @Operation(summary = "댓글 삭제", tags = "CommentController")
    @DeleteMapping("/{idx}")
    public CommonResponse<Long> deleteComment(@PathVariable Long idx) {
        Long deletedCommentIdx = commentService.deleteComment(idx);
        return new CommonResponse<>(HttpStatus.OK, deletedCommentIdx);
    }

    @Operation(summary = "게시물 내 댓글 조회", tags = "CommentController")
    @GetMapping("/list")
    public CommonResponse<List<CommentResponse>> getList(@RequestParam Long postIdx) {
        List<CommentResponse> responseList = commentService.getList(postIdx);
        return new CommonResponse<>(HttpStatus.OK, responseList);
    }
}
