package com.blog.demo.service;

import com.blog.demo.domain.Post;
import com.blog.demo.domain.PostTag;
import com.blog.demo.domain.Tag;
import com.blog.demo.repository.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostTagService {
    @Autowired
    private PostTagRepository postTagRepository;

    public Long join(PostTag postTag){
        postTagRepository.save(postTag);
        return postTag.getId();
    }

    public PostTag findOne(Long id) {
        return postTagRepository.findOne(id);
    }
    public List<PostTag> findPostTagsByPostId(Long postId){ return postTagRepository.findPostTagsByPostId(postId); }
    public List<PostTag> findPostTags() {
        return postTagRepository.findAll();
    }
    public void deleteOne(long id) { postTagRepository.deleteOne(id); }

    public List<PostTag> joinByTags(List<Tag> tags){
        List<PostTag> result = new ArrayList<>();
        tags.forEach(t->{
            PostTag newOne = new PostTag();
            newOne.setTag(t);
            join(newOne);
            result.add(newOne);
        });
        return result;
    }

    public List<PostTag> updatePostTag(Post post, List<Tag> tags) {

        List<PostTag> postTags = post.getPostTags();
        postTags.stream().forEach(pt->{
            deleteOne(pt.getId());
        });

        postTags.clear();

        tags.forEach(t->{
            PostTag newOne = new PostTag();
            newOne.setTag(t);
            join(newOne);
            postTags.add(newOne);
        });

        postTags.forEach(pt->pt.setPost(post));
        return postTags;
    }
}
