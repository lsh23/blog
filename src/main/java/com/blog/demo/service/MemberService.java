package com.blog.demo.service;

import com.blog.demo.domain.Member;
import com.blog.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public String join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    public void update(String id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

    public Member findOne(String id) {
        return memberRepository.findOne(id);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

}
