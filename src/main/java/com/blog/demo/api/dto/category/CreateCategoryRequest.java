package com.blog.demo.api.dto.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateCategoryRequest {
    @NotEmpty
    private String memberId;
    private Long parentId;
    @NotEmpty
    private String name;
}
