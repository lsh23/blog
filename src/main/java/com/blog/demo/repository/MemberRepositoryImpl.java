package com.blog.demo.repository;

import com.blog.demo.domain.Member;
import com.blog.demo.exception.NotFoundMemberException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository{

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findOneByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        List<Member> member = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
        return member.stream().findAny();
    }

    @Override
    public boolean existsByEmail(String email) {
        Optional<Member> member = findByEmail(email);
        return member.isPresent();
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        Optional<Member> member = findByLoginId(loginId);
        return member.isPresent();
    }

    private Optional<Member> findByLoginId(String loginId) {
        List<Member> member = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return member.stream().findAny();
    }


}
