package com.blog.demo.service;

import com.blog.demo.api.dto.category.CategoryDto;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.repository.CategoryRepository;
import com.blog.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    void save() throws Exception {

        // given
        Category category = Category.builder()
                .name("name")
                .build();

        // when
        Long categoryId = categoryService.save(category);

        // then
        assertThat(categoryRepository.findOne(categoryId).getName()).isEqualTo(category.getName());
    }

    @Test
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
    void findAll() throws Exception {
        // given
        List<String> nameList = new ArrayList<>(Arrays.asList("name1", "name2", "name3"));
        nameList.stream()
                .map(name -> Category.builder().name(name).build())
                .forEach(category -> categoryService.save(category));

        // when
        List<Category> all = categoryService.findAll();

        // then
        assertThat(all.size()).isEqualTo(nameList.size());
    }

    @Test
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

        childCategory1.updateParent(parentCategory1);
        childCategory2.updateParent(parentCategory2);

        categoryService.save(parentCategory1);
        categoryService.save(parentCategory2);
        categoryService.save(childCategory1);
        categoryService.save(childCategory2);

        // when
        List<CategoryDto> allRootCategories = categoryService.findAllRootCategories();

        // then
        assertThat(allRootCategories.size()).isEqualTo(2);
    }

    @Test
    void findCategoriesByMember() throws Exception {
        // given
        Member member = Member.builder().id("member1").build();
        memberRepository.save(member);

        Category category1 = Category.builder()
                .name("name1")
                .member(member)
                .build();
        Category category2 = Category.builder()
                .name("name2")
                .member(member)
                .build();
        categoryService.save(category1);
        categoryService.save(category2);

        // when
        List<CategoryDto> categoriesByMember = categoryService.findCategoriesByMember(member.getId());

        // then
        assertThat(categoriesByMember.size()).isEqualTo(2);
    }

    @Test
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
}