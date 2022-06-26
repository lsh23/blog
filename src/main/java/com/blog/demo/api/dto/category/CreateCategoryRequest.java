package com.blog.demo.api.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {
    @NotEmpty
    private String memberId;
    private Long parentId;
    @NotEmpty
    private String name;
}
