package com.blog.demo.api.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class UpdateCategoryRequest {
    @NotNull
    private Long id;
    private String memberId;
    private Long parentId;
    @NotEmpty
    private String name;
}
