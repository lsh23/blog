package com.blog.demo.domain;

import javax.persistence.*;

@Entity
public class Member {
    @Id @Column(name="MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
