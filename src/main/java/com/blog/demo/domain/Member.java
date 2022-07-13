package com.blog.demo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id @Column(name="MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    private String email;

    @Builder
    public Member(String name, String email, String password, OauthProvider oauthProvider){
        this.name = name;
        this.email = email;
        this.password = password;
        this.oauthProvider = oauthProvider;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
