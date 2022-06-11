package com.blog.demo.api.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCategoryResponse {
    private Long id;
    private String name;
}
