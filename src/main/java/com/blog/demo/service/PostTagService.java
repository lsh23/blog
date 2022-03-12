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
    private PostTagRepository PostTagRepository;

    public Long join(PostTag postTag){
        PostTagRepository.save(postTag);
        return postTag.getId();
    }

    public PostTag findOne(long id) {
        return PostTagRepository.findOne(id);
    }
    public List<PostTag> findCategories() {
        return PostTagRepository.findAll();
    }
}
