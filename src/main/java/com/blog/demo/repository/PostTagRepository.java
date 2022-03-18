package com.blog.demo.repository;

import com.blog.demo.domain.PostTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostTagRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(PostTag postTag){
        em.persist(postTag);
    }

    public List<PostTag> findAll(){
        return em.createQuery("select pt from PostTag pt", PostTag.class)
                .getResultList();
    }

    public PostTag findOne(Long id){
        return em.find(PostTag.class, id);
    }
}
