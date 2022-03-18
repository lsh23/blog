package com.blog.demo.service;

import com.blog.demo.domain.PostTag;
import com.blog.demo.repository.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostTagService {
    @Autowired
    private PostTagRepository postTagRepository;

    public Long join(PostTag postTag){
        postTagRepository.save(postTag);
        return postTag.getId();
    }

    public PostTag findOne(long id) {
        return postTagRepository.findOne(id);
    }
    public List<PostTag> findPostTags() {
        return postTagRepository.findAll();
    }

}
