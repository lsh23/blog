package com.blog.demo.service;

import com.blog.demo.domain.PostTag;
import com.blog.demo.domain.Tag;
import com.blog.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagService {
    @Autowired
    private TagRepository TagRepository;

    public Long join(Tag tag){
        TagRepository.save(tag);
        return tag.getId();
    }

    public Tag findOne(long id) {
        return TagRepository.findOne(id);
    }
    public List<Tag> findTags() {
        return TagRepository.findAll();
    }

    public void deleteOne(Long id) { TagRepository.deleteOne(id);}
}
