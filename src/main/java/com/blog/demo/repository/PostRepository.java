package com.blog.demo.repository;

import com.blog.demo.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
