package com.study.jinius.account.controller;

import com.study.jinius.account.model.*;
import com.study.jinius.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "회원가입", tags = "AccountController")
    @PostMapping("sign-up")
    public ResponseEntity<AccountSignUpResponse> signUp(@RequestBody AccountCreateRequest request) {
        AccountSignUpParam param = request.toParam();
        AccountSignUpResponse response = accountService.signUp(param);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "로그인", tags = "AccountController")
    @GetMapping("sign-in")
    public ResponseEntity<AccountSignInResponse> signIn(@ModelAttribute AccountSignInRequest request) {
        AccountSignInParam param = request.toParam();
        AccountSignInResponse response = accountService.signIn(param);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }
}
