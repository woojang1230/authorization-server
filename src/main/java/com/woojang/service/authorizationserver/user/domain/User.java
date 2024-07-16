package com.woojang.service.authorizationserver.user.domain;

import java.util.List;

import javax.persistence.*;

import com.woojang.service.authorizationserver.cmm.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(
        name = "user",
        indexes = {
                @Index(
                        name = "USER_INDEX_1",
                        columnList = "username",
                        unique = true
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Authority> authorities;

    private User(final String username, final List<Authority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }
}
