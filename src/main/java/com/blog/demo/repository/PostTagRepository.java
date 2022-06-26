package com.blog.demo.repository;

import com.blog.demo.domain.PostTag;
import java.util.List;
import java.util.Optional;

public interface PostTagRepository{

    PostTag save(PostTag postTag);

    void deleteById(Long id);

    Optional<PostTag> findById(Long id);

    List<PostTag> findAll();

    List<PostTag> findPostTagsByPostId(Long postId);


}
