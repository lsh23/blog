package com.blog.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {
    @Id @Column(name="MEMBER_ID")
    private String id;
    private String name;
    private String password;
}
