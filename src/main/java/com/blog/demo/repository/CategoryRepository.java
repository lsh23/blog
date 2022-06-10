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

    public List<Category> findAllRootCategories(){
        return em.createQuery("select c from Category c where c.parent is null ", Category.class)
                .getResultList();
    }

    public Category findOne(Long id){
        return em.find(Category.class, id);
    }

    public void deleteOne(Long id) {
        Category deletedOne = findOne(id);
        em.remove(deletedOne);
    }

    public List<Category> findCategoriesByMember(String memberId) {
        return em.createQuery(
                "select c from Category c" +
                            " join fetch c.member m "+
                            " where m.id =:memberId", Category.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }
}
