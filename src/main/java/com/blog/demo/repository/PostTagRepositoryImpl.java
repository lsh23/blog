package com.blog.demo.repository;

import com.blog.demo.domain.PostTag;
import com.blog.demo.exception.NotFoundPostTagException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class PostTagRepositoryImpl implements PostTagRepository{
    @PersistenceContext
    private EntityManager em;

    public PostTag save(PostTag postTag){
        em.persist(postTag);
        return postTag;
    }

    public List<PostTag> findAll(){
        return em.createQuery("select pt from PostTag pt", PostTag.class)
                .getResultList();
    }

    public Optional<PostTag> findById(Long id){
        PostTag postTag = em.find(PostTag.class, id);
        return Optional.ofNullable(postTag);
    }

    public List<PostTag> findPostTagsByPostId(Long postId) {
        return em.createQuery("select pt from PostTag pt where pt.post.id =: postId", PostTag.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void deleteById(Long id) {
        PostTag deletedOne = findById(id).orElseThrow(()->new NotFoundPostTagException());
        em.remove(deletedOne);
    }
}
