package com.blog.demo.repository;

import com.blog.demo.domain.Comment;
import com.blog.demo.exception.NotFoundCommentException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    @PersistenceContext
    private EntityManager em;

    public Comment save(Comment comment){
        em.persist(comment);
        return comment;
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

    public Optional<Comment> findById(Long id){
        Comment comment = em.find(Comment.class, id);
        return Optional.ofNullable(comment);
    }

    public void deleteById(Long id) {
        Comment comment = findById(id)
                .orElseThrow(()->new NotFoundCommentException());
        em.remove(comment);
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
