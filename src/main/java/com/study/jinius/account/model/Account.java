package com.study.jinius.account.model;

import com.study.jinius.common.model.BaseEntity;
import com.study.jinius.common.model.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @Column(unique = true)
    private String stringId;

    private String password;

    private String name;

    private LocalDateTime lastSignedInDate;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    public Account(String stringId, String password, String name) {
        this.stringId = stringId;
        this.password = password;
        this.name = name;
        this.role = Role.USER;
    }
}
