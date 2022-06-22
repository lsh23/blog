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
        List<Comment> resultList = em.createQuery("select c from Comment c join fetch c.member m", Comment.class)
                .getResultList();
        return resultList;
    }

    public List<Comment> findAllRootComment(){
        List<Comment> resultList = em.createQuery("select c from Comment c join fetch c.member m where c.parent is null ", Comment.class)
                .getResultList();
        return resultList;
    }

    public Comment findOne(Long id){
        return em.find(Comment.class, id);
    }

    public void deleteOne(Long id) {
        Comment deleteOne = findOne(id);
        em.remove(deleteOne);
    }

    public List<Comment> findAllByPostId(Long postId) {
        return em.createQuery(
                        "select c from Comment c "+
                                " join fetch c.member m" +
                                " where c.post.id =: postId " +
                                " and c.parent is null", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<Comment> findAllRootCommentByPostId(Long postId) {
        return em.createQuery(
                        "select c from Comment c "+
                                " join fetch c.member m" +
                                " where c.post.id =: postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
