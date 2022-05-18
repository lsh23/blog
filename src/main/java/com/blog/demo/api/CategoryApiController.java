package com.blog.demo.api;


import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Post;
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

    @PatchMapping("/{id}")
    public UpdateCategoryResponse updateCategory(@RequestBody @Valid UpdateCategoryRequest updateCategoryRequest, @PathVariable("id") Long id){
        Category category = categoryService.findOne(id);

        Member findMember = memberService.findOne(updateCategoryRequest.getUser_id());
        category.setMember(findMember);

        if(updateCategoryRequest.getParent_id() != null){
            Category findCategory = categoryService.findOne(updateCategoryRequest.getParent_id());
            category.setParent(findCategory);
        }

        category.setName(updateCategoryRequest.getName());
        return new UpdateCategoryResponse(category.getId(),category.getName());
    }

    @DeleteMapping("/{id}")
    public DeleteCategoryResponse deleteCategory(@PathVariable("id") Long id){
        Category category = categoryService.findOne(id);
        categoryService.deleteOne(id);
        return new DeleteCategoryResponse(category.getId(), category.getName());
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

    @Data
    @AllArgsConstructor
    static class UpdateCategoryResponse {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateCategoryRequest {
        @NotEmpty
        private String user_id;
        private Long parent_id;
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class DeleteCategoryResponse {
        private Long id;
        private String name;
    }
}
