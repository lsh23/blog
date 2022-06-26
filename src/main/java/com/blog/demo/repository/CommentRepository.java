package com.blog.demo.repository;

import com.blog.demo.domain.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    void deleteById(Long id);

    Optional<Comment> findById(Long id);

    List<Comment> findAll();

    List<Comment> findAllRootComment();

    List<Comment> findAllByPostId(Long postId);

    List<Comment> findAllRootCommentByPostId(Long postId);
}
