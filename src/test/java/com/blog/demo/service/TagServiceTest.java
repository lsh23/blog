package com.blog.demo.service;

import com.blog.demo.api.dto.tag.CreateTagRequest;
import com.blog.demo.api.dto.tag.TagDto;
import com.blog.demo.api.dto.tag.UpdateTagRequest;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Tag;
import com.blog.demo.repository.MemberRepository;
import com.blog.demo.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    MemberRepository memberRepository;


    @Autowired
    TagService tagService;

    @Test
    @DisplayName("Tag 단순 저장")
    void saveTest() {
        //given
        Tag tag = Tag.builder().name("tag").build();

        //when
        tagService.save(tag);

        //then
        assertThat(tagRepository.findOne(tag.getId())).isEqualTo(tag);

    }

    @Test
    @DisplayName("Tag 단순 조회")
    void findOneTest() {
        //given
        Tag tag = Tag.builder().name("tag").build();
        tagRepository.save(tag);

        //when
        TagDto one = tagService.findOne(tag.getId());

        //then
        assertThat(one.getId()).isEqualTo(tag.getId());
    }

    @Test
    @DisplayName("Tag 리스트 조회")
    void findAllTest() {
        //given
        Tag tag1 = Tag.builder().name("tag1").build();
        Tag tag2 = Tag.builder().name("tag2").build();
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        //when
        List<TagDto> all = tagService.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Tag 삭제")
    void deleteOneTest() {
        //given
        Tag tag = Tag.builder().name("tag").build();
        tagRepository.save(tag);

        //when
        tagService.deleteOne(tag.getId());

        //then
        assertThat(tagRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("요청에 의한 Tag 생성")
    void createTagTest() {
        //given
        Member member = Member.builder().id("member").build();
        memberRepository.save(member);
        CreateTagRequest createTagRequest = new CreateTagRequest(member.getId(),"tag");

        //when
        TagDto tag = tagService.createTag(createTagRequest);

        //then
        assertThat(tagRepository.findOne(tag.getId()).getName()).isEqualTo("tag");
    }

    @Test
    @DisplayName("Tag 수정")
    void updateTagTest() {
        //given
        Member member = Member.builder().id("member").build();
        memberRepository.save(member);
        CreateTagRequest createTagRequest = new CreateTagRequest(member.getId(),"tag");
        TagDto tag = tagService.createTag(createTagRequest);

        //when
        UpdateTagRequest updateTagRequest = new UpdateTagRequest("member", "tag2");
        TagDto tagDto = tagService.updateTag(tag.getId(), updateTagRequest);

        //then
        assertThat(tagRepository.findOne(tagDto.getId()).getName()).isEqualTo("tag2");
    }

    @Test
    @DisplayName("Tag bulk update")
    void bulkSearchAndIfNoneCreateTest() {
        //given
        Member member = Member.builder().id("member").build();
        memberRepository.save(member);

        CreateTagRequest createTagRequest = new CreateTagRequest(member.getId(),"tag");
        TagDto tag1Dto = tagService.createTag(createTagRequest);

        //when
        Tag tag2 = Tag.builder().name("tag2").build();
        Tag tag3 = Tag.builder().name("tag3").build();
        List<TagDto> tagDtos = new ArrayList<>();
        TagDto tag2Dto = new TagDto(tag2);
        tag2Dto.setId(-1L);
        TagDto tag3Dto = new TagDto(tag3);
        tag3Dto.setId(-1L);
        tagDtos.add(tag1Dto);
        tagDtos.add(tag2Dto);
        tagDtos.add(tag3Dto);
        tagService.bulkSearchAndIfNoneCreate(member.getId(), tagDtos);

        //then
        assertThat(tagRepository.findAll().size()).isEqualTo(3);
    }
}