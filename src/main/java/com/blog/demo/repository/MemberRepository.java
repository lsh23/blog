package com.blog.demo.repository;

import com.blog.demo.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(String id);

    List<Member> findAll();

    List<Member> findOneByName(String name) ;
}
