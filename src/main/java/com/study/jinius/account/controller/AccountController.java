package com.study.jinius.account.controller;

import com.study.jinius.account.model.AccountCreateParam;
import com.study.jinius.account.model.AccountCreateRequest;
import com.study.jinius.account.model.AccountCreateResponse;
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
    public CommonResponse<AccountCreateResponse> create(@RequestBody AccountCreateRequest request) {
        AccountCreateParam param = request.toParam();
        AccountCreateResponse response = accountService.create(param);
        return new CommonResponse<>(HttpStatus.OK, response);
    }
}
