package com.blog.demo.api.dto.category;

import com.blog.demo.api.CategoryApiController;
import com.blog.demo.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CategoryDto {

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.child = category.getChild().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    private Long id;
    private String name;
    private List<CategoryDto> child;
}
