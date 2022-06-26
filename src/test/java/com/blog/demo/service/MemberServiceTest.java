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

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("Member 단순 저장 - 성공")
    void saveTest_success() {
        // given
        Member member = Member.builder()
                .name("member")
                .build();
        memberRepository.save(member);

        // when
        Member findOne = memberService.findById(member.getId());

        // then
        assertThat(findOne).isEqualTo(member);
    }

    @Test
    @DisplayName("요청을 통한 Member 생성 - 성공")
    void createUserTest_success() {
        // given
        CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                .name("name")
                .password("password")
                .build();

        // when
        CreateMemberResponse CreateMemberResponse = memberService.createMember(createMemberRequest);
        Member member = memberRepository.findById(CreateMemberResponse.getId()).get();

        // then
        assertThat(member.getName()).isEqualTo("name");
        assertThat(member.getPassword()).isEqualTo("password");
    }

    @Test
    @DisplayName("요청을 통한 Member 수정 - 성공")
    void updateTest_success() {
        // given
        Member member = Member.builder()
                .name("member")
                .build();
        memberRepository.save(member);

        // when
        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
                .name("change")
                .build();

        memberService.updateMember(member.getId(), updateMemberRequest);

        // then
        assertThat(member.getName()).isEqualTo("change");
    }

    @Test
    @DisplayName("Member 단순 조회 - 성공")
    void findOneTest_success() {
        // given
        Member member = Member.builder()
                .name("member")
                .build();
        memberRepository.save(member);

        // when
        Member findOne = memberService.findById(member.getId());

        // then
        assertThat(findOne).isEqualTo(member);
    }

    @Test
    @DisplayName("Member 전체 조회 - 성공")
    void findAllTest_success() {
        // given
        Member member1 = Member.builder()
                .name("member1")
                .build();
        Member member2 = Member.builder()
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