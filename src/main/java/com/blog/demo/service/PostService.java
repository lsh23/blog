package com.blog.demo.service;

import com.blog.demo.api.dto.TagDto;
import com.blog.demo.domain.*;
import com.blog.demo.repository.PostRepository;
import com.blog.demo.repository.PostSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final PostTagService postTagService;

    public PostService(PostRepository postRepository, MemberService memberService, CategoryService categoryService, com.blog.demo.service.TagService tagService, PostTagService postTagService) {
        this.postRepository = postRepository;
        this.memberService = memberService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.postTagService = postTagService;
    }

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

    public Post writePost(String title, String contents, String memberId, Long categoryId, List<TagDto> tagDtos) {
        Member member = memberService.findOne(memberId);
        Category category = categoryService.findOne(categoryId);

        List<Tag> tags = tagService.bulkSearchAndIfNoneCreate(tagDtos, member);
        List<PostTag> postTags = postTagService.joinByTags(tags);

        Post post = Post.createPost(title, contents, member, category, postTags);
        postRepository.save(post);

        return post;
    }

    public Post updatePost(Long postId, String title, String contents, String memberId, Long categoryId, List<TagDto> tagDtos) {
        Post post = postRepository.findOne(postId);

        Member member = memberService.findOne(memberId);
        Category category = categoryService.findOne(categoryId);
        List<Tag> tags = tagService.bulkSearchAndIfNoneCreate(tagDtos, member);
        List<PostTag> postTags = postTagService.updatePostTag(post,tags);

        post.setTitle(title);
        post.setContent(contents);
        post.setMember(member);
        post.setCategory(category);

        return post;
    }
}
