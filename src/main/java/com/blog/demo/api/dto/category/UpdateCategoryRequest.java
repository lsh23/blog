package com.blog.demo.api.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UpdateCategoryRequest {
    @NotEmpty
    private Long id;
    private String memberId;
    private Long parentId;
    @NotEmpty
    private String name;
}
