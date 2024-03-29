package com.blog.demo.service;

import com.blog.demo.api.dto.tag.CreateTagRequest;
import com.blog.demo.api.dto.tag.TagDto;
import com.blog.demo.api.dto.tag.UpdateTagRequest;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Tag;
import com.blog.demo.repository.MemberRepository;
import com.blog.demo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;

    public TagService(TagRepository tagRepository, MemberRepository memberRepository) {
        this.tagRepository = tagRepository;
        this.memberRepository = memberRepository;
    }

    public Long save(Tag tag){
        tagRepository.save(tag);
        return tag.getId();
    }

    public TagDto findOne(long id) {
        Tag one = tagRepository.findOne(id);
        return new TagDto(one);
    }
    public List<TagDto> findAll() {
        List<Tag> all = tagRepository.findAll();
        return all.stream().map(TagDto::new).collect(Collectors.toList());
    }

    public TagDto deleteOne(Long id) {
        Tag deleteOne = tagRepository.deleteOne(id);
        return new TagDto(deleteOne);
    }

    public List<TagDto> findAll(String memberId) {
        return getAllTag(memberId)
                .stream()
                .map(TagDto::new)
                .collect(Collectors.toList());
    }

    private List<Tag> getAllTag(String memberId){
        if (memberId == null){
            return tagRepository.findAll();
        }
        return tagRepository.findAllByMemberId(memberId);
    }

    public TagDto createTag(CreateTagRequest createTagRequest) {
        String name = createTagRequest.getName();
        String memberId = createTagRequest.getMemberId();
        Member findMember = memberRepository.findOne(memberId);
        Tag tag = Tag.builder()
                .name(name)
                .member(findMember)
                .build();
        tagRepository.save(tag);
        return new TagDto(tag);
    }

    public TagDto updateTag(Long tagId, UpdateTagRequest updateTagRequest) {
        String name = updateTagRequest.getName();
        Tag tag = tagRepository.findOne(tagId);
        tag.updateName(name);
        return new TagDto(tag);
    }

    public List<TagDto> bulkSearchAndIfNoneCreate(String memberId, List<TagDto> tagDtos) {
        Member member = memberRepository.findOne(memberId);

        List<Tag> result = new ArrayList<>();
        tagDtos.stream()
                .forEach(t->{
                    if (t.getId() != -1){
                        result.add(tagRepository.findOne(t.getId()));
                    }else{
                        Tag newOne = Tag.builder()
                                .name(t.getName())
                                .member(member)
                                .build();
                        tagRepository.save(newOne);
                        result.add(newOne);
                    }
        });
        return result.stream().map(t-> new TagDto(t)).collect(Collectors.toList());
    }
}
