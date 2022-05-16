package com.blog.demo.service;

import com.blog.demo.domain.Category;
import com.blog.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Long join(Category category){
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
    public List<Category> findAllRootCategories() {return categoryRepository.findAllRootCategories(); }

    @Transactional(readOnly = true)
    public List<Category> findCategoriesWithMember(String MemberId){ return categoryRepository.findCategoriesWithMember(MemberId);}

    public void deleteOne(Long id) { categoryRepository.deleteOne(id);}
}
