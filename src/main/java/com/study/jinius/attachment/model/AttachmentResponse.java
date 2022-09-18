package com.study.jinius.attachment.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttachmentResponse {
    private Long idx;
    private String uploadName;
    private String message;

    // 정상 케이스
    public static AttachmentResponse from(Attachment attachment) {
        AttachmentResponse response = new AttachmentResponse();
        response.setIdx(attachment.getIdx());
        response.setUploadName(attachment.getUploadName());
        response.setMessage(Result.SUCCESS.getMessage());

        return response;
    }

    // 오류 발생 케이스
    public static AttachmentResponse from(MultipartFile file, Result result) {
        AttachmentResponse response = new AttachmentResponse();
        response.setIdx(-1L);
        response.setUploadName(file.getName());
        response.setMessage(result.getMessage());

        return response;
    }
}
