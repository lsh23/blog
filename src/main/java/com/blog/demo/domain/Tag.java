package com.blog.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {
    @Id @Column(name="TAG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();

    @Builder
    public Tag(String name, Member member) {
        this.name = name;
        this.member = member;
    }

    public void updateName(String name) {
        this.name = name;
    }

}
