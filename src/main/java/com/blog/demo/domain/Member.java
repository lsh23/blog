package com.blog.demo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    @Column(nullable = true, length = 30, unique = true)
    private String loginId;

    @Column(nullable = true, unique = true)
    private String email;

    @Builder
    public Member(String name, String email, String loginId, String password, OauthProvider oauthProvider) {
        this.name = name;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
        this.oauthProvider = oauthProvider;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
