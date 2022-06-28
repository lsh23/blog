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

    @Builder
    public Member(String name, String password){
        this.name = name;
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
