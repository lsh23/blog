package com.blog.demo.service;

import com.blog.demo.api.dto.member.CreateMemberRequest;
import com.blog.demo.api.dto.member.CreateMemberResponse;
import com.blog.demo.api.dto.member.MemberDto;
import com.blog.demo.api.dto.member.UpdateMemberRequest;
import com.blog.demo.domain.Member;
import com.blog.demo.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("Member 단순 저장")
    void saveTest() {
        // given
        Member member = Member.builder()
                .id("id")
                .name("member")
                .build();
        memberRepository.save(member);

        // when
        Member findOne = memberService.findOne("id");

        // then
        assertThat(findOne).isEqualTo(member);
    }

    @Test
    @DisplayName("요청을 통한 Member 생성")
    void createUserTest() {
        // given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest();
        createMemberRequest.setId("id");
        createMemberRequest.setName("name");
        createMemberRequest.setPassword("password");

        // when
        CreateMemberResponse CreateMemberResponse = memberService.createMember(createMemberRequest);
        Member member = memberRepository.findOne("id");

        // then
        assertThat(member.getId()).isEqualTo("id");
        assertThat(member.getName()).isEqualTo("name");
        assertThat(member.getPassword()).isEqualTo("password");
    }

    @Test
    @DisplayName("요청을 통한 Member 수정")
    void updateTest() {
        // given
        Member member = Member.builder()
                .id("id")
                .name("member")
                .build();
        memberRepository.save(member);

        // when
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest();
        updateMemberRequest.setName("change");
        memberService.updateMember("id", updateMemberRequest);

        // then
        assertThat(member.getName()).isEqualTo("change");
    }

    @Test
    @DisplayName("Member 단순 조회")
    void findOneTest() {
        // given
        Member member = Member.builder()
                .id("id")
                .name("member")
                .build();
        memberRepository.save(member);

        // when
        Member findOne = memberService.findOne("id");

        // then
        assertThat(findOne).isEqualTo(member);
    }

    @Test
    @DisplayName("Member 전체 조회")
    void findAllTest() {
        // given
        Member member1 = Member.builder()
                .id("id1")
                .name("member1")
                .build();
        Member member2 = Member.builder()
                .id("id2")
                .name("member2")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<MemberDto> all = memberService.findAll();

        // then
        assertThat(all.size()).isEqualTo(2);
    }
}