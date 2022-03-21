package com.blog.demo.repository;

import com.blog.demo.domain.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Category category){
        em.persist(category);
    }

    public List<Category> findAll(){
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    public Category findOne(Long id){
        return em.find(Category.class, id);
    }

    public void deleteOne(Long id) {
        Category deletedOne = findOne(id);
        em.remove(deletedOne);
    }
}
