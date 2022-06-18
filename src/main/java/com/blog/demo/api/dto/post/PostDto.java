package com.blog.demo.api.dto.post;

import com.blog.demo.api.dto.category.CategoryDto;
import com.blog.demo.api.dto.posttag.PostTagDto;
import com.blog.demo.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private CategoryDto category;
    private List<PostTagDto> postTags;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getUpdatedAt();
        this.category = new CategoryDto(post.getCategory());
        this.postTags = PostTagDto.toList(post.getPostTags());
    }
}
