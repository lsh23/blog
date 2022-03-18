package com.blog.demo.service;

import com.blog.demo.domain.Comment;
import com.blog.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Long join(Comment comment){
        commentRepository.save(comment);
        return comment.getId();
    }

    public Comment findOne(long id) {
        return commentRepository.findOne(id);
    }
    public List<Comment> findComments() {
        return commentRepository.findAll();
    }

}
