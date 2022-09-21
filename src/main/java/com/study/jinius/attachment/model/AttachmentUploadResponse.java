package com.study.jinius.attachment.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttachmentUploadResponse {
    private Long idx;
    private String uploadName;
    private String message;

    // 정상 케이스
    public static AttachmentUploadResponse from(Attachment attachment) {
        AttachmentUploadResponse response = new AttachmentUploadResponse();
        response.setIdx(attachment.getIdx());
        response.setUploadName(attachment.getUploadName());
        response.setMessage(Result.SUCCESS.getMessage());

        return response;
    }

    // 오류 발생 케이스
    public static AttachmentUploadResponse from(MultipartFile file, Result result) {
        AttachmentUploadResponse response = new AttachmentUploadResponse();
        response.setIdx(-1L);
        response.setUploadName(file.getName());
        response.setMessage(result.getMessage());

        return response;
    }
}
