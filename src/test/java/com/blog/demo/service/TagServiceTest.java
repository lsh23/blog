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

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("Tag 단순 저장 - 성공")
    void saveTest_success() {
        //given
        Tag tag = Tag.builder().name("tag").build();

        //when
        tagService.save(tag);

        //then
        assertThat(tagRepository.findById(tag.getId()).get()).isEqualTo(tag);

    }

    @Test
    @DisplayName("Tag 단순 조회 - 성공")
    void findOneTest_success() {
        //given
        Tag tag = Tag.builder().name("tag").build();
        tagRepository.save(tag);

        //when
        TagDto one = tagService.findById(tag.getId());

        //then
        assertThat(one.getId()).isEqualTo(tag.getId());
    }

    @Test
    @DisplayName("Tag 리스트 조회 - 성공")
    void findAllTest_success() {
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
    @DisplayName("Tag 삭제 - 성공")
    void deleteOneTest_success() {
        //given
        Tag tag = Tag.builder().name("tag").build();
        tagRepository.save(tag);

        //when
        tagService.deleteById(tag.getId());

        //then
        assertThat(tagRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("요청에 의한 Tag 생성 - 성공")
    void createTagTest_success() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);
        CreateTagRequest createTagRequest = CreateTagRequest.builder()
                .memberId(member.getId())
                .name("tag")
                .build();

        //when
        TagDto tag = tagService.createTag(createTagRequest);

        //then
        assertThat(tagRepository.findById(tag.getId()).get().getName()).isEqualTo("tag");
    }

    @Test
    @DisplayName("Tag 수정 - 성공")
    void updateTagTest_success() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);
        CreateTagRequest createTagRequest = CreateTagRequest.builder()
                .memberId(member.getId())
                .name("tag")
                .build();
        TagDto tag = tagService.createTag(createTagRequest);

        //when
        UpdateTagRequest updateTagRequest = UpdateTagRequest
                .builder()
                .memberId(1L)
                .name("tag2")
                .build();

        TagDto tagDto = tagService.updateTag(tag.getId(), updateTagRequest);

        //then
        assertThat(tagRepository.findById(tagDto.getId()).get().getName()).isEqualTo("tag2");
    }

    @Test
    @DisplayName("Tag bulk update - 성공")
    void bulkSearchAndIfNoneCreateTest_success() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);

        CreateTagRequest createTagRequest = CreateTagRequest.builder()
                .memberId(member.getId())
                .name("tag")
                .build();

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