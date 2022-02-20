package com.blog.demo.domain;

import javax.persistence.*;

@Entity
public class PostTag {
    @Id @Column(name="POST_TAG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;
}
