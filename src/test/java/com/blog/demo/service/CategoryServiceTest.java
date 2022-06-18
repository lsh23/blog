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
    @DisplayName("category 저장")
    void save() throws Exception {

        // given
        Category category = Category.builder()
                .name("name")
                .build();

        // when
        Long categoryId = categoryService.save(category);

        // then
        assertThat(categoryRepository.findOne(categoryId).getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("category 단일 조회")
    void findOne() throws Exception {
        // given
        Category category = Category.builder()
                .name("name")
                .build();

        Long categoryId = categoryService.save(category);

        // when
        Category findOne = categoryService.findOne(categoryId);

        // then
        assertThat(findOne.getId()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("category 모든 리스트 조회")
    void findAll() throws Exception {
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
    @DisplayName("category root 리스트 조회")
    void findAllRootCategories() throws Exception {
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
    @DisplayName("category root 리스트 by member 조회")
    void findCategoriesByMember() throws Exception {
        // given
        Member member = Member.builder().id("member1").build();
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
    @DisplayName("category 단일 삭제")
    void deleteOne() throws Exception {
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
    @DisplayName("category 생성")
    void createCategory() throws Exception {
        // given
        Member member = Member.builder().id("member1").build();
        memberRepository.save(member);

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setMemberId(member.getId());
        createCategoryRequest.setName("category");

        // when
        CategoryDto category = categoryService.createCategory(createCategoryRequest);

        // then
        assertThat(categoryRepository.findOne(category.getId()).getName()).isEqualTo("category");
    }


    @Test
    @DisplayName("category 수정")
    void updateCategory() throws Exception {
        // given
        Member member = Member.builder().id("member1").build();
        memberRepository.save(member);

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setMemberId(member.getId());
        createCategoryRequest.setName("category");
        CategoryDto category = categoryService.createCategory(createCategoryRequest);


        // when
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(category.getId(), member.getId(), null, "category2");
        CategoryDto categoryDto = categoryService.updateCategory(updateCategoryRequest,category.getId());

        // then
        assertThat(categoryRepository.findOne(category.getId()).getName()).isEqualTo("category2");
    }
}