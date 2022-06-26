package com.blog.demo.repository;

import com.blog.demo.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    void deleteById(Long id);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    List<Post> findPosts(PostSearch postSearch);
}
