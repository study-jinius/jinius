package com.study.jinius.account.model;

import com.study.jinius.common.model.BaseEntity;
import com.study.jinius.common.model.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @Size(min = 8, max = 30)
    @Column(unique = true, length = 30)
    private String stringId;

    @Column(nullable = false)
    private String password;

    @Size(min = 1, max = 10)
    @Column(nullable = false, length = 10)
    private String name;

    private LocalDateTime lastSignedInDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    public Account(String stringId, String password, String name) {
        this.stringId = stringId;
        this.password = password;
        this.name = name;
        this.role = Role.USER;
    }
}
