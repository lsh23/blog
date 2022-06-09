package com.blog.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {
    @Id @Column(name="COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PARENT_ID")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> child = new ArrayList<>();

    @Builder
    public Comment(String text, Member member, Post post, Comment parent) {
        this.text = text;
        this.member = member;
        this.post = post;
        this.parent = parent;
    }

    public void updateText(String text){
        this.text = text;
    }

    public void assignParent(Comment parent) {
        this.parent = parent;
    }

}
