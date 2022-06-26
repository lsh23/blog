package com.blog.demo.repository;

import com.blog.demo.domain.Tag;
import com.blog.demo.exception.NotFoundTagException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository{
    @PersistenceContext
    private EntityManager em;

    public Tag save(Tag tag){
        em.persist(tag);
        return tag;
    }

    public List<Tag> findAll(){
        return em.createQuery("select t from Tag t", Tag.class)
                .getResultList();
    }

    public Optional<Tag> findById(Long id){
        Tag tag = em.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    public void deleteById(Long id) {
        Tag deletedOne = findById(id).orElseThrow(NotFoundTagException::new);
        em.remove(deletedOne);
    }

    public List<Tag> findAllByMemberId(String memberId) {
        return em.createQuery("select t from Tag t where t.member.id =: memberId", Tag.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
