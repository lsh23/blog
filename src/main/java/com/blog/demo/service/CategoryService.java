package com.blog.demo.service;

import com.blog.demo.api.dto.category.CategoryDto;
import com.blog.demo.api.dto.category.CreateCategoryRequest;
import com.blog.demo.api.dto.category.UpdateCategoryRequest;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.exception.NotFoundCategoryException;
import com.blog.demo.exception.NotFoundMemberException;
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
    public Category findById(long id) {
        Category category= categoryRepository.findById(id)
                .orElseThrow(NotFoundCategoryException::new);
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllRootCategories(String memberId) {
        return getRootCategories(memberId).stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    private List<Category> getRootCategories(String memberId) {
        if (memberId == null) {
            return categoryRepository.findAllRootCategories();
        }
        return categoryRepository.findAllRootCategoriesByMember(memberId);
    }

    public void deleteOne(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest) {
        Member member = memberRepository.findById(createCategoryRequest.getMemberId())
                .orElseThrow(NotFoundCategoryException::new);

        Category category = Category.builder()
                .member(member)
                .name(createCategoryRequest.getName())
                .build();

        if(createCategoryRequest.getParentId() != null){
            Category findCategory = findById(createCategoryRequest.getParentId());
            category.assignParent(findCategory);
        }

        categoryRepository.save(category);

        return new CategoryDto(category.getId(),category.getName());
    }

    public CategoryDto updateCategory(UpdateCategoryRequest updateCategoryRequest, Long id) {
        Category category = findById(id);

        Member findMember = memberRepository.findById(updateCategoryRequest.getMemberId())
                .orElseThrow(NotFoundMemberException::new);
        category.updateMember(findMember);

        category.updateName(updateCategoryRequest.getName());

        if(updateCategoryRequest.getParentId() != null){
            Category findCategory = findById(updateCategoryRequest.getParentId());
            category.assignParent(findCategory);
        }

        return new CategoryDto(category.getId(),category.getName());
    }
}
