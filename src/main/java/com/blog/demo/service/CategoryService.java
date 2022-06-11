package com.blog.demo.service;

import com.blog.demo.api.dto.category.CategoryDto;
import com.blog.demo.api.dto.category.CreateCategoryRequest;
import com.blog.demo.api.dto.category.UpdateCategoryRequest;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.repository.CategoryRepository;
import com.blog.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public CategoryService(CategoryRepository categoryRepository, MemberRepository memberRepository) {
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    public Long save(Category category){
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional(readOnly = true)
    public Category findOne(long id) {
        return categoryRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllRootCategories() {
        List<Category> allRootCategories = categoryRepository.findAllRootCategories();
        List<CategoryDto> categoryDtos = allRootCategories.stream()
                .map(c -> new CategoryDto(c))
                .collect(Collectors.toList());
        return categoryDtos;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllRootCategories(String memberId) {
        List<Category> allRootCategories;
        allRootCategories = categoryRepository.findAllRootCategoriesByMember(memberId);
        List<CategoryDto> categoryDtos = allRootCategories.stream()
                .map(c -> new CategoryDto(c))
                .collect(Collectors.toList());
        return categoryDtos;
    }

    public CategoryDto deleteOne(Long id) {
        Category deleteOne = categoryRepository.deleteOne(id);
        return new CategoryDto(deleteOne.getId(), deleteOne.getName());
    }

    public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest) {
        Member findMember = memberRepository.findOne(createCategoryRequest.getMemberId());

        Category category = Category.builder()
                .member(findMember)
                .name(createCategoryRequest.getName())
                .build();

        if(createCategoryRequest.getParentId() != null){
            Category findCategory = findOne(createCategoryRequest.getParentId());
            category.assignParent(findCategory);
        }

        categoryRepository.save(category);

        return new CategoryDto(category.getId(),category.getName());
    }

    public CategoryDto updateCategory(UpdateCategoryRequest updateCategoryRequest, Long id) {
        Category category = findOne(id);

        Member findMember = memberRepository.findOne(updateCategoryRequest.getMemberId());
        category.updateMember(findMember);

        category.updateName(updateCategoryRequest.getName());

        if(updateCategoryRequest.getParentId() != null){
            Category findCategory = findOne(updateCategoryRequest.getParentId());
            category.assignParent(findCategory);
        }

        return new CategoryDto(category.getId(),category.getName());
    }
}
