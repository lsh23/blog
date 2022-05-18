package com.blog.demo.service;

import com.blog.demo.domain.Post;
import com.blog.demo.repository.PostRepository;
import com.blog.demo.repository.PostSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Long join(Post post){
        postRepository.save(post);
        return post.getId();
    }

    public Post findOne(long id) {
        return postRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Post> findPosts(PostSearch postSearch) {
        return postRepository.findPosts(postSearch);
    }

    public void deleteOne(long id) { postRepository.deleteOne(id); }

}
