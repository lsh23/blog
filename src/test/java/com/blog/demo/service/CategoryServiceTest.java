package com.blog.demo.service;

import com.blog.demo.api.dto.category.CategoryDto;
import com.blog.demo.api.dto.category.CreateCategoryRequest;
import com.blog.demo.api.dto.category.UpdateCategoryRequest;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.repository.CategoryRepository;
import com.blog.demo.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("category 저장 - 성공")
    void saveCategoryTest_success() throws Exception {

        // given
        Category category = Category.builder()
                .name("name")
                .build();

        // when
        Long categoryId = categoryService.save(category);

        // then
        assertThat(categoryRepository.findById(categoryId).get().getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("category 단일 조회 - 성공")
    void findOneTest_success() throws Exception {
        // given
        Category category = Category.builder()
                .name("name")
                .build();

        Long categoryId = categoryService.save(category);

        // when
        Category findOne = categoryService.findById(categoryId);

        // then
        assertThat(findOne.getId()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("category 모든 리스트 조회 - 성공")
    void findAllTest_success() throws Exception {
        // given
        Category parentCategory1 = Category.builder()
                .name("parent1")
                .build();
        Category parentCategory2 = Category.builder()
                .name("parent2")
                .build();
        Category childCategory1 = Category.builder()
                .name("child1")
                .build();
        Category childCategory2 = Category.builder()
                .name("child2")
                .build();

        childCategory1.assignParent(parentCategory1);
        childCategory2.assignParent(parentCategory2);

        categoryService.save(parentCategory1);
        categoryService.save(parentCategory2);
        categoryService.save(childCategory1);
        categoryService.save(childCategory2);

        // when
        List<Category> all = categoryService.findAll();

        // then
        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("category root 리스트 조회 - 성공")
    void findAllRootCategoriesTest_success() throws Exception {
        // given
        Category parentCategory1 = Category.builder()
                .name("parent1")
                .build();
        Category parentCategory2 = Category.builder()
                .name("parent2")
                .build();
        Category childCategory1 = Category.builder()
                .name("child1")
                .build();
        Category childCategory2 = Category.builder()
                .name("child2")
                .build();

        childCategory1.assignParent(parentCategory1);
        childCategory2.assignParent(parentCategory2);

        categoryService.save(parentCategory1);
        categoryService.save(parentCategory2);
        categoryService.save(childCategory1);
        categoryService.save(childCategory2);

        // when
        List<CategoryDto> allRootCategories = categoryService.findAllRootCategories(null);

        // then
        assertThat(allRootCategories.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("category root 리스트 by member 조회 - 성공")
    void findCategoriesByMemberTest_success() throws Exception {
        // given
        Member member = Member.builder().build();
        memberRepository.save(member);

        Category category1 = Category.builder()
                .name("parent1")
                .member(member)
                .build();
        Category category2 = Category.builder()
                .name("child1")
                .member(member)
                .build();

        category2.assignParent(category1);

        categoryService.save(category1);
        categoryService.save(category2);

        // when
        List<CategoryDto> categoriesByMember = categoryService.findAllRootCategories(member.getId());

        // then
        assertThat(categoriesByMember.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("category 단일 삭제 - 성공")
    void deleteOneTest_success() throws Exception {
        // given
        Category category = Category.builder()
                .name("name")
                .build();

        Long categoryId = categoryService.save(category);
        // when
        categoryService.deleteOne(categoryId);

        // then
        assertThat(categoryService.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("category 생성 - 성공")
    void createCategoryTest_success() throws Exception {
        // given
        Member member = Member.builder().build();
        memberRepository.save(member);

        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .memberId(member.getId())
                .name("category")
                .build();

        // when
        CategoryDto category = categoryService.createCategory(createCategoryRequest);

        // then
        assertThat(categoryRepository.findById(category.getId()).get().getName()).isEqualTo("category");
    }


    @Test
    @DisplayName("category 수정 - 성공")
    void updateCategoryTest_success() throws Exception {
        // given
        Member member = Member.builder().build();
        memberRepository.save(member);

        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .memberId(member.getId())
                .name("category")
                .build();
        CategoryDto category = categoryService.createCategory(createCategoryRequest);


        // when
        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .id(category.getId())
                .memberId(member.getId())
                .parentId(null)
                .name("category2")
                .build();

        CategoryDto categoryDto = categoryService.updateCategory(updateCategoryRequest,category.getId());

        // then
        assertThat(categoryRepository.findById(category.getId()).get().getName()).isEqualTo("category2");
    }
}