package com.study.jinius.account.controller;

import com.study.jinius.account.model.*;
import com.study.jinius.account.service.AccountService;
import com.study.jinius.common.model.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "회원가입", tags = "AccountController")
    @PostMapping("sign-up")
    public CommonResponse<AccountSignUpResponse> signUp(@RequestBody AccountCreateRequest request) {
        AccountSignUpParam param = request.toParam();
        AccountSignUpResponse response = accountService.signUp(param);
        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @GetMapping("sign-in")
    public CommonResponse<AccountSignInResponse> signIn(@ModelAttribute AccountSignInRequest request) {
        AccountSignInParam param = request.toParam();
        AccountSignInResponse response = accountService.signIn(param);
        return new CommonResponse<>(HttpStatus.OK, response);
    }
}
