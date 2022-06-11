package com.blog.demo.api.dto.post;

import com.blog.demo.api.dto.category.CategoryDto;
import com.blog.demo.api.dto.posttag.PostTagDto;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.PostTag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private CategoryDto category;
    private List<PostTagDto> postTags;

    public PostDto(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, Category category, List<PostTag> postTags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.category = new CategoryDto(category);
        this.postTags = postTags.stream().map(pt -> new PostTagDto(pt)).collect(Collectors.toList());
    }
}
