package com.blog.demo.api;


import com.blog.demo.api.dto.Result;
import com.blog.demo.api.dto.category.*;
import com.blog.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryApiController {
    private final CategoryService categoryService;

    @GetMapping
    public Result getCategory(@RequestParam(required = false, value = "id") Long memberId){
        List<CategoryDto> categoryDtos = categoryService.findAllRootCategories(memberId);
        return new Result(categoryDtos.size(), categoryDtos);
    }

    @PostMapping
    public CreateCategoryResponse createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest){
        CategoryDto categoryDto = categoryService.createCategory(createCategoryRequest);
        return new CreateCategoryResponse(categoryDto.getId(),categoryDto.getName());
    }

    @PatchMapping("/{id}")
    public UpdateCategoryResponse updateCategory(@RequestBody @Valid UpdateCategoryRequest updateCategoryRequest, @PathVariable("id") Long categoryId){
        CategoryDto categoryDto = categoryService.updateCategory(updateCategoryRequest, categoryId);
        return new UpdateCategoryResponse(categoryDto.getId(),categoryDto.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long categoryId){
        categoryService.deleteOne(categoryId);
        return ResponseEntity.noContent().build();
    }

}
