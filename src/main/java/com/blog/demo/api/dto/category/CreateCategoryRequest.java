package com.blog.demo.api.dto.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public
class CreateCategoryRequest {
    @NotEmpty
    private String user_id;
    private Long parent_id;
    @NotEmpty
    private String name;
}
