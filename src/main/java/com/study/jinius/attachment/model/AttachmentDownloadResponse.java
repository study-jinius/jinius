package com.study.jinius.attachment.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.UrlResource;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class AttachmentDownloadResponse {
    private UrlResource resource;
    private String uploadName;
    private String mediaType;

    public static AttachmentDownloadResponse from(Attachment attachment, UrlResource resource) {
        AttachmentDownloadResponse response = new AttachmentDownloadResponse();
        response.setResource(resource);
        response.setMediaType(attachment.getMediaType());
        response.setUploadName(changeUploadName(attachment.getUploadName()));

        return response;
    }

    // 브라우저 별 인코딩 방식이 다르다(현재는 크롬 기준). 리팩 시 위치 고민하기
    // https://developersp.tistory.com/14
    private static String changeUploadName(String uploadName) {
        if (StringUtils.isBlank(uploadName)) {
            // TODO: 예외처리
        }

        return new String(uploadName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }
}
