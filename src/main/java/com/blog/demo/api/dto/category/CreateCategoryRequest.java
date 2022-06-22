package com.blog.demo.api.dto.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CreateCategoryRequest {
    @NotEmpty
    private String memberId;
    private Long parentId;
    @NotEmpty
    private String name;
}
