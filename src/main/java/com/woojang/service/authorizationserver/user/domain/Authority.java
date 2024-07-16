package com.woojang.service.authorizationserver.user.domain;

import javax.persistence.*;

import com.woojang.service.authorizationserver.cmm.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Authority(final String name) {
        this.name = name;
    }

    public static Authority makeGuestAuthority(final String name) {
        return new Authority(name);
    }
}
