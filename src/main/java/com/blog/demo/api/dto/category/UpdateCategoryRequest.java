package com.blog.demo.api.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public
class UpdateCategoryRequest {
    @NotEmpty
    private String user_id;
    private Long parent_id;
    @NotEmpty
    private String name;
}
