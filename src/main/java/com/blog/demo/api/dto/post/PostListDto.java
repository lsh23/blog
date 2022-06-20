package com.blog.demo.api.dto.post;

import com.blog.demo.api.dto.posttag.PostTagDto;
import com.blog.demo.domain.Post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostListDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String categoryName;
    private List<PostTagDto> postTags;

    public PostListDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getUpdatedAt();
        this.categoryName = post.getCategory().getName();
        this.postTags = PostTagDto.toList(post.getPostTags());
    }
}
