package com.blog.demo.repository;

import com.blog.demo.domain.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Tag tag){
        em.persist(tag);
    }

    public List<Tag> findAll(){
        return em.createQuery("select t from Tag t", Tag.class)
                .getResultList();
    }

    public Tag findOne(Long id){
        return em.find(Tag.class, id);
    }

    public List<Tag> findByPostId(Long postId) {
        return em.createQuery("select t from Tag t where PostTag.id =", Tag.class)
                .getResultList();
    }
}
