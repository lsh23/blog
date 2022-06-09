package com.blog.demo.api;


import com.blog.demo.api.dto.Result;
import com.blog.demo.api.dto.category.*;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.service.CategoryService;
import com.blog.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryApiController {
    private final CategoryService categoryService;
    private final MemberService memberService;

    @GetMapping
    public Result getCategory(@RequestParam(required = false, value = "id") String memberId){

        if (memberId == null){
            List<Category> categories = categoryService.findAllRootCategories();
            List<CategoryDto> categoryDtos = categories.stream()
                    .map(c -> new CategoryDto(c))
                    .collect(Collectors.toList());
            return new Result(categoryDtos.size(), categoryDtos);
        }

        List<Category> categories = categoryService.findCategoriesWithMember(memberId);
        List<CategoryDto> categoryDtos = categories.stream()
                .filter(c -> c.getParent() == null)
                .map(c -> new CategoryDto(c))
                .collect(Collectors.toList());
        return new Result(categoryDtos.size(), categoryDtos);
    }

    @PostMapping
    public CreateCategoryResponse createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest){

        Member findMember = memberService.findOne(createCategoryRequest.getUser_id());

        Category.CategoryBuilder builder = Category.builder()
                .member(findMember)
                .name(createCategoryRequest.getName());

        if(createCategoryRequest.getParent_id() != null){
            Category findCategory = categoryService.findOne(createCategoryRequest.getParent_id());
            builder.parent(findCategory);
        }

        Category category = builder.build();
        categoryService.save(category);

        return new CreateCategoryResponse(category.getId(),category.getName());
    }

    @PatchMapping("/{id}")
    public UpdateCategoryResponse updateCategory(@RequestBody @Valid UpdateCategoryRequest updateCategoryRequest, @PathVariable("id") Long id){
        Category category = categoryService.findOne(id);

        Member findMember = memberService.findOne(updateCategoryRequest.getUser_id());
        category.updateMember(findMember);

        category.updateName(updateCategoryRequest.getName());

        if(updateCategoryRequest.getParent_id() != null){
            Category findCategory = categoryService.findOne(updateCategoryRequest.getParent_id());
            category.updateParent(findCategory);
        }

        return new UpdateCategoryResponse(category.getId(),category.getName());
    }

    @DeleteMapping("/{id}")
    public DeleteCategoryResponse deleteCategory(@PathVariable("id") Long id){
        Category category = categoryService.findOne(id);
        categoryService.deleteOne(id);
        return new DeleteCategoryResponse(category.getId(), category.getName());
    }

}
