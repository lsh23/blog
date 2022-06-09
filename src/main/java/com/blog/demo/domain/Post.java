package com.blog.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
    @Id @Column(name="POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post (String title, String content, Member member, Category category, List<PostTag> postTags){
        this.title = title;
        this.content= content;
        this.member = member;
        this.category = category;
        postTags.forEach(pt->pt.assignPost(this));
    }

    public void updateAll(String title, String content, Member member, Category category, List<PostTag> postTags){
        this.title = title;
        this.content= content;
        this.member = member;
        this.category = category;
        postTags.forEach(pt->pt.assignPost(this));
    }
}
