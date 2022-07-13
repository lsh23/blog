package com.blog.demo.service;

import com.blog.demo.api.dto.auth.OauthCreateMemberRequest;
import com.blog.demo.api.dto.member.CreateMemberRequest;
import com.blog.demo.api.dto.member.CreateMemberResponse;
import com.blog.demo.api.dto.member.MemberDto;
import com.blog.demo.api.dto.member.UpdateMemberRequest;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.OauthProvider;
import com.blog.demo.exception.DuplicateEmailException;
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

    public Long save(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public CreateMemberResponse createMember(CreateMemberRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .password(request.getPassword())
                .build();
        memberRepository.save(member);

        return CreateMemberResponse.from(member);
    }

    public MemberDto updateMember(Long id, UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);

        String name = updateMemberRequest.getName();
        member.updateName(name);

        return new MemberDto(member.getId(), member.getName());
    }

    public Member findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);
        return member;
    }

    public List<MemberDto> findAll() {
        return memberRepository.findAll().stream()
                .map(m -> new MemberDto(m.getId(),m.getName()))
                .collect(Collectors.toList());
    }

    public CreateMemberResponse createMemberByOauth(OauthCreateMemberRequest oauthCreateMemberRequest) {
        String email = oauthCreateMemberRequest.getEmail();
        OauthProvider oauthProvider = OauthProvider.valueOfWithIgnoreCase(oauthCreateMemberRequest.getOauthProvider());

        validateDuplicateEmail(email);

        Member member = Member.builder()
                .email(email)
                .oauthProvider(oauthProvider)
                .build();

        Member saveMember = memberRepository.save(member);
        return CreateMemberResponse.from(saveMember);
    }

    @Transactional(readOnly = true)
    public void validateDuplicateEmail(final String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }
}
