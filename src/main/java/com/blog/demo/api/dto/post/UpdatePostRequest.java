package com.blog.demo.api.dto.post;

import com.blog.demo.api.dto.tag.TagDto;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePostRequest {
    private String userId;
    private String title;
    private String contents;
    private Long categoryId;
    private List<TagDto> tags;
}
