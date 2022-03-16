package com.blog.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Post {
    @Id @Column(name="POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID")
    private Category category;

    @ManyToOne
    @JoinColumn(name="POST_TAG_ID")
    private PostTag postTag;


}
