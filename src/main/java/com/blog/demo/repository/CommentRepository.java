package com.blog.demo.repository;

import com.blog.demo.domain.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Comment comment){
        em.persist(comment);
    }

    public List<Comment> findAll(){
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public Comment findOne(Long id){
        return em.find(Comment.class, id);
    }
}
