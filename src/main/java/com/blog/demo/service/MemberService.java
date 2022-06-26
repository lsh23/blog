package com.blog.demo.service;

import com.blog.demo.api.dto.member.CreateMemberRequest;
import com.blog.demo.api.dto.member.CreateMemberResponse;
import com.blog.demo.api.dto.member.MemberDto;
import com.blog.demo.api.dto.member.UpdateMemberRequest;
import com.blog.demo.domain.Member;
import com.blog.demo.exception.NotFoundMemberException;
import com.blog.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String save(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public CreateMemberResponse createMember(CreateMemberRequest request) {
        Member member = Member.builder()
                .id(request.getId())
                .name(request.getName())
                .password(request.getPassword())
                .build();
        memberRepository.save(member);

        return new CreateMemberResponse(member.getId());
    }

    public MemberDto updateMember(String id, UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);

        String name = updateMemberRequest.getName();
        member.updateName(name);

        return new MemberDto(member.getId(), member.getName());
    }

    public Member findById(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);
        return member;
    }

    public List<MemberDto> findAll() {
        return memberRepository.findAll().stream()
                .map(m -> new MemberDto(m.getId(),m.getName()))
                .collect(Collectors.toList());
    }

}
