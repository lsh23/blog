package com.blog.demo.api.dto.post;

import com.blog.demo.api.dto.posttag.PostTagDto;
import com.blog.demo.domain.PostTag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public
class PostListDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String categoryName;
    private List<PostTagDto> postTags;

    public PostListDto(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String categoryName, List<PostTag> postTags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.categoryName = categoryName;
        this.postTags = postTags.stream().map(pt -> new PostTagDto(pt)).collect(Collectors.toList());
    }
}
