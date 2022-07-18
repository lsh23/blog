package com.blog.demo.repository;

import com.blog.demo.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    List<Member> findOneByName(String name) ;

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
