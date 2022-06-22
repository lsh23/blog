package com.blog.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {
    @Id @Column(name="CATEGORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> child = new ArrayList<>();

    @Builder
    public Category(String name, Member member, Category parent){
        this.name = name;
        this.member = member;
        this.parent = parent;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateMember(Member member) {
        this.member = member;
    }

    public void assignParent(Category parent) {
        if (this.parent != null){
            this.parent.getChild().remove(this);
        }
        this.parent = parent;
        parent.getChild().add(this);
    }
}
