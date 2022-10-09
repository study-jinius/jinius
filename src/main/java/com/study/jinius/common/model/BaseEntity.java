package com.study.jinius.common.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// @EntityListeners : JPA Entity에서 이벤트가 발생할 때마다 특정 로직을 실행시킬 수 있는 어노테이션
// AuditingEntityListener의 touchForCreate, touchForUpdate가 수행된다.
@Getter
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    protected LocalDateTime createDate;

    @LastModifiedDate
    protected LocalDateTime updateDate;
}
