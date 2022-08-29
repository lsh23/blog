package com.blog.demo.service;

import com.blog.demo.api.dto.category.*;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.exception.NotFoundCategoryException;
import com.blog.demo.exception.NotFoundMemberException;
import com.blog.demo.repository.CategoryRepository;
import com.blog.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public List<CategoryDto> findAllRootCategories(Long memberId) {
        return getRootCategories(memberId).stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    private List<Category> getRootCategories(Long memberId) {
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

    public void saveCategory(BulkCategorySaveRequest bulkCategorySaveRequest) {
        Long memberId = bulkCategorySaveRequest.getMemberId();

        Member member = memberRepository.findById(bulkCategorySaveRequest.getMemberId())
                .orElseThrow(NotFoundCategoryException::new);

        List<BulkCategoryDto> bulkCategoryDtos = bulkCategorySaveRequest.getBulkCategoryDtos();
        for (BulkCategoryDto bulkCategoryDto: bulkCategoryDtos) {
            saveCategoryRecursive(bulkCategoryDto, null, member);
        }

        List<BulkCategoryDto> DeletedBulkCategoryDtos = bulkCategorySaveRequest.getDeletedBulkCategoryDtos();
        for (BulkCategoryDto DeletedBulkCategoryDto: DeletedBulkCategoryDtos) {
            deleteCategory(DeletedBulkCategoryDto);
        }
    }

    private void deleteCategory(BulkCategoryDto deletedBulkCategoryDto) {
        if (isNewCategory(deletedBulkCategoryDto) == false){
            long id = Long.parseLong(deletedBulkCategoryDto.getId());
            Optional<Category> category = categoryRepository.findById(id);
            if(category.isPresent()) {
                Category c = category.get();
                c.getParent().getChild().remove(c);
            }
        }
    }

    private void saveCategoryRecursive(BulkCategoryDto bulkCategoryDto, Category parent, Member member){
        List<BulkCategoryDto> children = bulkCategoryDto.getChild();

        if (children.isEmpty()){
            saveCategory(bulkCategoryDto, parent, member);
        }else{
            Category category = saveCategory(bulkCategoryDto, parent, member);
            for (BulkCategoryDto child:children) {
                saveCategoryRecursive(child, category, member);
            }
        }
    }

    private Category saveCategory(BulkCategoryDto bulkCategoryDto,Category parent, Member member){
        if (isNewCategory(bulkCategoryDto)){
            Category category = Category.builder()
                    .member(member)
                    .name(bulkCategoryDto.getName())
                    .build();
            if(parent != null){
                category.assignParent(parent);
            }
            return categoryRepository.save(category);
        }
        else{
            long id = Long.parseLong(bulkCategoryDto.getId());
            Category category = findById(id);
            category.updateName(bulkCategoryDto.getName());
            if(parent != null){
                category.assignParent(parent);
            }
            return category;
        }
    }

    private boolean isNewCategory(BulkCategoryDto bulkCategoryDto) {
        try {
            Long.parseLong(bulkCategoryDto.getId());
            return false;
        } catch(NumberFormatException e){
            return true;
        }
    }
}
