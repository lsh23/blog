package com.blog.demo.api.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class DeleteCategoryResponse {
    private Long id;
    private String name;
}
