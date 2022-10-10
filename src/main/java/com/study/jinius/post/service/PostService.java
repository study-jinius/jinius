package com.study.jinius.post.service;

import com.study.jinius.account.model.Account;
import com.study.jinius.account.repository.AccountRepository;
import com.study.jinius.account.service.CustomUserDetailsService;
import com.study.jinius.common.exception.BadRequestException;
import com.study.jinius.common.exception.NotFoundException;
import com.study.jinius.common.model.Status;
import com.study.jinius.post.model.*;
import com.study.jinius.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CustomUserDetailsService customUserDetailsService;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public PostCreateResponse createPost(PostCreateParam param) {
        Long accountId = customUserDetailsService.getAccountIdx();
        Account account = getValidAccount(accountId);

        Post post = param.toPost(account);
        Post result = postRepository.save(post);

        return PostCreateResponse.from(result);
    }

    public PostUpdateResponse updatePost(Long idx, PostUpdateParam param) {
        Long accountId = customUserDetailsService.getAccountIdx();
        checkIsValidAccountId(accountId);

        Post post = postRepository.findById(idx)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));
        checkPostOwner(accountId, post.getAccount().getIdx());

        post.updatePost(param.getContent(), param.getStatus());
        Post result = postRepository.save(post);

        return PostUpdateResponse.from(result);
    }

    public Long deletePost(Long idx) {
        Long accountId = customUserDetailsService.getAccountIdx();
        checkIsValidAccountId(accountId);

        Post post = postRepository.findById(idx)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));
        checkPostOwner(accountId, post.getAccount().getIdx());

        post.deletePost();
        Post result = postRepository.save(post);

        return result.getIdx();
    }

    public List<PostResponse> getList() {
        Long accountId = customUserDetailsService.getAccountIdx();
        Account account = getValidAccount(accountId);

        List<Post> postList = postRepository.findAllByAccount(account);

        return postList.stream()
                .filter(post -> !Status.DELETED.equals(post.getStatus()))
                .map(PostResponse::from).toList();
    }

    private void checkPostOwner(Long accountIdx, Long postOwnerId) {
        if (!Objects.equals(accountIdx, postOwnerId)) {
            throw new BadRequestException("게시물 작성자가 아닙니다.");
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
