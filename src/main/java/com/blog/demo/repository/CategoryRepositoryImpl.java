package com.blog.demo.repository;

import com.blog.demo.domain.Category;
import com.blog.demo.exception.NotFoundCategoryException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    @PersistenceContext
    private EntityManager em;

    public Category save(Category category){
        em.persist(category);
        return category;
    }

    public List<Category> findAll(){
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    public List<Category> findAllRootCategories(){
        return em.createQuery("select c from Category c where c.parent is null", Category.class)
                .getResultList();
    }

    public Optional<Category> findById(Long id){
        Category category = em.find(Category.class, id);
        return Optional.ofNullable(category);
    }

    public void deleteById(Long id) {
        Category category = findById(id)
                .orElseThrow(NotFoundCategoryException::new);
        em.remove(category);
    }

    public List<Category> findAllRootCategoriesByMember(Long memberId) {
        return em.createQuery(
                "select c from Category c" +
                            " join fetch c.member m "+
                            " where m.id =:memberId" +
                            " and c.parent is null", Category.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }
}
