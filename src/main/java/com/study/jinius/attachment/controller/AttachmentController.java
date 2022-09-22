package com.study.jinius.attachment.controller;

import com.study.jinius.attachment.exception.UnsupportedFileException;
import com.study.jinius.attachment.model.AttachmentDownloadResponse;
import com.study.jinius.attachment.model.AttachmentUploadResponse;
import com.study.jinius.attachment.service.AttachmentService;
import com.study.jinius.common.model.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Operation(summary = "파일 업로드", tags = "AttachmentController")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<List<AttachmentUploadResponse>> upload(@RequestParam(required = false) MultipartFile[] files) {
        try {
            List<AttachmentUploadResponse> responseList = attachmentService.upload(files);
            return new CommonResponse<>(HttpStatus.OK, responseList);
        } catch (IOException e) {
            return new CommonResponse<>(HttpStatus.BAD_REQUEST, null);
        }
    }


    @Operation(summary = "파일 다운로드", tags = "AttachmentController")
    @GetMapping("/download/{storedName}")
    public ResponseEntity<Resource> download(@PathVariable String storedName) {
        try {
            AttachmentDownloadResponse response = attachmentService.download(storedName);
            // CONTENT_DISPOSITION : response에 오는 컨텐츠의 성향을 나타낸다.
            // 아래의 경우 body의 값을 다운로드 받으라는 뜻이 된다.
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(response.getMediaType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + response.getUploadName())
                    .body(response.getResource());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler(value = UnsupportedFileException.class)
    public CommonResponse<String> handleUnsupportedFileException(UnsupportedFileException e) {
        return new CommonResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
