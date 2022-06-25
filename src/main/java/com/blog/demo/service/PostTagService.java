package com.blog.demo.service;

import com.blog.demo.api.dto.posttag.PostTagDto;
import com.blog.demo.api.dto.tag.TagDto;
import com.blog.demo.domain.Post;
import com.blog.demo.domain.PostTag;
import com.blog.demo.domain.Tag;
import com.blog.demo.exception.NotFoundPostException;
import com.blog.demo.exception.NotFoundPostTagException;
import com.blog.demo.exception.NotFoundTagException;
import com.blog.demo.repository.PostRepository;
import com.blog.demo.repository.PostTagRepository;
import com.blog.demo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostTagService {

    private final PostTagRepository postTagRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostTagService(PostTagRepository postTagRepository, PostRepository postRepository, TagRepository tagRepository) {
        this.postTagRepository = postTagRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Long save(PostTag postTag){
        postTagRepository.save(postTag);
        return postTag.getId();
    }

    public PostTagDto findById(Long id) {
        PostTag findOne = postTagRepository.findById(id)
                .orElseThrow(()->new NotFoundPostTagException());
        return new PostTagDto(findOne);
    }

    public List<PostTagDto> findPostTags(Long postId) {
        List<PostTag> all = getPostTags(postId);
        return all.stream().map(PostTagDto::new).collect(Collectors.toList());
    }

    private List<PostTag> getPostTags(Long postId){
        if (postId == null){
            return postTagRepository.findAll();
        }
        return postTagRepository.findPostTagsByPostId(postId);
    }

    public void deleteOne(Long id) {
        postTagRepository.deleteById(id);
    }

    public List<PostTagDto> saveByTags(Long postId, List<TagDto> tagDtos){
        List<PostTag> result = new ArrayList<>();
        tagDtos.forEach(t->{
            Tag tag = tagRepository.findById(t.getId())
                    .orElseThrow(()->new NotFoundTagException());
            PostTag newOne = PostTag.builder()
                    .tag(tag)
                    .build();
            postTagRepository.save(newOne);
            result.add(newOne);
        });

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException());

        result.forEach(pt->pt.assignPost(post));
        return result.stream().map(PostTagDto::new).collect(Collectors.toList());
    }

    public List<PostTagDto> updatePostTag(Long postId, List<TagDto> tagDtos) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new NotFoundPostException());

        List<PostTag> postTags = post.getPostTags();
        postTags.stream().forEach(pt->{
            postTagRepository.deleteById(pt.getId());
        });

        postTags.clear();

        List<PostTag> newPostTags = new ArrayList<>();

        tagDtos.forEach(t->{
            Tag tag = tagRepository.findById(t.getId())
                    .orElseThrow(()-> new NotFoundTagException());
            PostTag newOne = PostTag.builder()
                    .tag(tag)
                    .build();
            postTagRepository.save(newOne);
            newPostTags.add(newOne);
        });

        newPostTags.forEach(pt->pt.assignPost(post));
        return newPostTags.stream().map(PostTagDto::new).collect(Collectors.toList());
    }
}
