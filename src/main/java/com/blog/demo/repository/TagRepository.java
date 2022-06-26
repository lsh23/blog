package com.blog.demo.repository;

import com.blog.demo.domain.Tag;
import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    void deleteById(Long id);

    Optional<Tag> findById(Long id);

    List<Tag> findAll();

    List<Tag> findAllByMemberId(Long memberId);
}
