package com.study.jinius.comment.service;

import com.study.jinius.comment.model.*;
import com.study.jinius.comment.repository.CommentRepository;
import com.study.jinius.common.model.Status;
import com.study.jinius.post.model.Post;
import com.study.jinius.post.model.UpdatePostParam;
import com.study.jinius.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CreateCommentResponse createComment(CreateCommentParam param) {
        Comment comment = param.toComment();

        Post post = postRepository.findById(param.getPostId()).orElseThrow();
        post.addComment(comment);
        comment.setPost(post);

        Long parentId = param.getParentId();
        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId).orElseThrow();
            comment.setParent(parentComment);
        }

        Comment result = commentRepository.save(comment);
        return CreateCommentResponse.from(result);
    }


    @Transactional
    public UpdateCommentResponse updateComment(Long idx, UpdatePostParam param) {
        Comment comment = commentRepository.findById(idx).orElseThrow();
        comment.updateComment(param.getContent(), param.getStatus());

        Post post = comment.getPost();
        if (Status.DELETED.equals(post.getStatus())) {
            // TODO: 예외처리
        }

        Comment result = commentRepository.save(comment);
        return UpdateCommentResponse.from(result);
    }


    public Long deleteComment(Long idx) {
        Comment comment = commentRepository.findById(idx).orElseThrow();
        comment.deleteComment();
        Comment result = commentRepository.save(comment);

        return result.getIdx();
    }

    public List<CommentResponse> getList(Long postIdx) {
        Post post = postRepository.findById(postIdx).orElseThrow();

        if (Status.DELETED.equals(post.getStatus())) {
            // TODO: 예외처리
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponse> responseList = new ArrayList<>();
        makeCommentResponseList(commentList, responseList);

        return responseList;
    }

    // TODO : 무한 대댓글 조회할 수 있는 방법 찾기
    private void makeCommentResponseList(List<Comment> commentList, List<CommentResponse> responseList) {
        for (Comment comment : commentList) {
            responseList.add(CommentResponse.from(comment));

            List<Comment> childList = comment.getChildList();
            if (CollectionUtils.isEmpty(childList)) {
                makeCommentResponseList(childList, responseList);
            }
        }
    }
}
