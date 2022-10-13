package com.study.jinius.comment.service;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.account.service.CustomUserDetailsService;
import com.study.jinius.comment.model.*;
import com.study.jinius.comment.repository.CommentRepository;
import com.study.jinius.common.exception.BadRequestException;
import com.study.jinius.common.exception.NotFoundException;
import com.study.jinius.common.model.Status;
import com.study.jinius.post.model.Post;
import com.study.jinius.post.model.PostUpdateParam;
import com.study.jinius.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CustomUserDetailsService customUserDetailsService;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateCommentResponse createComment(CommentCreateParam param) {
        Long accountId = customUserDetailsService.getAccountIdx();
        Account account = getValidAccount(accountId);
        Comment comment = param.toComment(account);

        Post post = postRepository.findById(param.getPostId())
                .filter(Post::exists)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));
        post.addComment(comment);
        comment.setPost(post);

        Long parentId = param.getParentId();
        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId)
                    .filter(Comment::exists)
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 기존 댓글입니다."));
            comment.setParent(parentComment);
        }

        Comment result = commentRepository.save(comment);
        return CreateCommentResponse.from(result);
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long idx, PostUpdateParam param) {
        Long accountId = customUserDetailsService.getAccountIdx();
        checkIsValidAccountId(accountId);

        Comment comment = commentRepository.findById(idx)
                .filter(Comment::exists)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));

        checkCommentOwner(accountId, comment.getAccount().getIdx());
        comment.updateComment(param.getContent(), param.getStatus());

        Post post = comment.getPost();
        if (!post.exists()) throw new NotFoundException("존재하지 않는 게시물입니다.");

        Comment result = commentRepository.save(comment);
        return CommentUpdateResponse.from(result);
    }

    @Transactional
    public Long deleteComment(Long idx) {
        Long accountId = customUserDetailsService.getAccountIdx();
        checkIsValidAccountId(accountId);

        Comment comment = commentRepository.findById(idx)
                .filter(Comment::exists)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));

        Post post = comment.getPost();
        if (!post.exists()) throw new NotFoundException("존재하지 않는 게시물입니다.");

        checkCommentOwner(accountId, comment.getAccount().getIdx());
        comment.deleteComment();

        Comment result = commentRepository.save(comment);
        return result.getIdx();
    }

    public List<CommentResponse> getList(Long postIdx) {
        Post post = postRepository.findById(postIdx)
                .filter(Post::exists)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        Long accountId = customUserDetailsService.getAccountIdx();

        List<Comment> commentList = commentRepository.findAllByPost(post).stream()
                .filter(c -> Objects.equals(c.getAccount().getIdx(), accountId)
                        || Status.ACTIVE.equals(c.getStatus()))
                .toList();
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

    private void checkCommentOwner(Long accountIdx, Long commentOwnerId) {
        if (!Objects.equals(accountIdx, commentOwnerId)) {
            throw new BadRequestException("댓글 작성자가 아닙니다.");
        }
    }

    private void checkIsValidAccountId(Long accountId) {
        getValidAccount(accountId);
    }

    private Account getValidAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .filter(Account::isValidAccount)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }
}
