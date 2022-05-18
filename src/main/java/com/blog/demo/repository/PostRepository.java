package com.blog.demo.repository;

import com.blog.demo.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PostRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Post post){
        em.persist(post);
    }

    public List<Post> findAll(){
        return em.createQuery("select p from Post p join fetch p.member join fetch p.category", Post.class)
                .getResultList();
    }

    public Post findOne(Long id){
        return em.find(Post.class, id);
    }

    public void deleteOne(Long id) {
        Post removedOne = findOne(id);
        em.remove(removedOne);
    }

    public List<Post> findPosts(PostSearch postSearch) {
        String jpql = "select p from Post p join fetch p.member m join fetch p.category c";
        boolean isFirstCondition = true;

        if (postSearch.getMemberId() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.id = :memberId";
        }

        if (postSearch.getCategoryId() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " c.id = :categoryId";
        }

        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setMaxResults(1000);

        if (postSearch.getMemberId() != null) {
            query = query.setParameter("memberId", postSearch.getMemberId());
        }
        if (postSearch.getCategoryId() != null) {
            query = query.setParameter("categoryId", postSearch.getCategoryId());
        }

        return query.getResultList();
    }
}
