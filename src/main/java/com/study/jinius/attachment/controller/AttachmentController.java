package com.study.jinius.attachment.controller;

import com.study.jinius.attachment.model.AttachmentResponse;
import com.study.jinius.attachment.service.AttachmentService;
import com.study.jinius.common.model.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Operation(summary = "파일 업로드", tags = "AttachmentController")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<List<AttachmentResponse>> upload(@RequestParam(required = false) MultipartFile[] files) {
        try {
            List<AttachmentResponse> responseList = attachmentService.upload(files);
            return new CommonResponse<>(HttpStatus.OK, responseList);
        } catch (IOException e) {
            return new CommonResponse<>(HttpStatus.BAD_REQUEST, null);
        }
    }
}
