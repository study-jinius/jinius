package com.study.jinius.attachment.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// TODO: 게시물, 댓글, 업로드한 사용자 정보 추가
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {
    @Id
    @GeneratedValue
    private Long idx;

    private String uploadName;

    private String storedName;

    public Attachment(String uploadName, String storedName) {
        this.uploadName = uploadName;
        this.storedName = storedName;
    }
}
