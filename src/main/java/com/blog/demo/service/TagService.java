package com.blog.demo.service;

import com.blog.demo.api.dto.TagDto;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Tag;
import com.blog.demo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TagService {

    private final TagRepository TagRepository;

    public TagService(com.blog.demo.repository.TagRepository tagRepository) {
        TagRepository = tagRepository;
    }

    public Long join(Tag tag){
        TagRepository.save(tag);
        return tag.getId();
    }

    public Tag findOne(long id) {
        return TagRepository.findOne(id);
    }
    public List<Tag> findAll() {
        return TagRepository.findAll();
    }

    public void deleteOne(Long id) { TagRepository.deleteOne(id);}

    public List<Tag> findAllByMemberId(String memberId) {
        return TagRepository.findAllByMemberId(memberId);
    }

    public List<Tag> bulkSearchAndIfNoneCreate(List<TagDto> tags, Member member) {
        List<Tag> result = new ArrayList<>();
        tags.stream().forEach(t->{
            if (t.getId() != -1){
                result.add(findOne(t.getId()));
            }else{
                Tag newOne = new Tag();
                newOne.setName(t.getName());
                newOne.setMember(member);
                join(newOne);
                result.add(newOne);
            }
        });
        return result;
    }
}
