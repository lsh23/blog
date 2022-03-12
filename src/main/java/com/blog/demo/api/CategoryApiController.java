package com.blog.demo.api;


import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.service.CategoryService;
import com.blog.demo.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {
    private final CategoryService categoryService;
    private final MemberService memberService;

    @GetMapping("/api/v1/categories")
    public Result getCategories(@RequestParam("id") String id){
        List<Category> categories = categoryService.findCategories();

        if(id == null){
            List<CategoryDto> categoryDtos = categories.stream()
                    .filter(c -> c.getParent() == null)
                    .map(c -> new CategoryDto(c))
                    .collect(Collectors.toList());
            return new Result(categoryDtos.size(), categoryDtos);
        }

        else{
            Member findMember = memberService.findOne(id);
            List<CategoryDto> categoryDtos = categories.stream()
                    .filter(c -> c.getMember() == findMember)
                    .filter(c -> c.getParent() == null)
                    .map(c -> new CategoryDto(c))
                    .collect(Collectors.toList());
            return new Result(categoryDtos.size(), categoryDtos);
        }

    }


    @PostMapping("/api/v1/categories")
    public CreateCategoryResponse create(@RequestBody @Valid CreateCategoryRequest createCategoryRequest){
        Category category = new Category();


        Member findMember = memberService.findOne(createCategoryRequest.getUser_id());
        category.setMember(findMember);


        if(createCategoryRequest.getParent_id() != null){
            Category findCategory = categoryService.findOne(createCategoryRequest.getParent_id());
            category.setParent(findCategory);
        }

        category.setName(createCategoryRequest.getName());

        categoryService.join(category);

        return new CreateCategoryResponse(category.getId(),category.getName());
    }

    @Data
    static class CreateCategoryRequest {
        @NotEmpty
        private String user_id;
        private Long parent_id;
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateCategoryResponse {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class CategoryDto{

        public CategoryDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public CategoryDto(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.child = category.getChild().stream()
                    .map(c -> new CategoryDto(c.getId(),c.getName()))
                    .collect(Collectors.toList());
        }

        private Long id;
        private String name;
        private List<CategoryDto> child;
    }


}