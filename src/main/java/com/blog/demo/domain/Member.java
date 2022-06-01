package com.blog.demo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id @Column(name="MEMBER_ID")
    private String id;
    private String name;
    private String password;

    @Builder
    public Member(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
