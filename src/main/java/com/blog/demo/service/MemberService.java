package com.blog.demo.service;

import com.blog.demo.api.dto.member.CreateMemberRequest;
import com.blog.demo.api.dto.member.CreateMemberResponse;
import com.blog.demo.domain.Member;
import com.blog.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public CreateMemberResponse createUser(CreateMemberRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .build();
        memberRepository.save(member);

        return new CreateMemberResponse(member.getId());
    }

    public void update(String id, String name) {
        Member member = memberRepository.findOne(id);
        member.updateName(name);
    }

    public Member findOne(String id) {
        return memberRepository.findOne(id);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

}
