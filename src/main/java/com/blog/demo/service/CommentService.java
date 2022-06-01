package com.blog.demo.service;

import com.blog.demo.domain.Comment;
import com.blog.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public Long save(Comment comment){
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public Comment findOne(long id) {
        return commentRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
