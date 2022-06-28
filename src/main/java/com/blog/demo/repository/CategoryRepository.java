package com.blog.demo.repository;

import com.blog.demo.domain.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    void deleteById(Long id);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    List<Category> findAllRootCategories();

    List<Category> findAllRootCategoriesByMember(Long memberId);
}
