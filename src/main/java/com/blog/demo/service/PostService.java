package com.blog.demo.service;

import com.blog.demo.api.dto.TagDto;
import com.blog.demo.domain.*;
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
    @Autowired
    private MemberService memberService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService TagService;
    @Autowired
    private PostTagService postTagService;

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

        List<Tag> tags = TagService.bulkSearchAndIfNoneCreate(tagDtos, member);
        List<PostTag> postTags = postTagService.joinByTags(tags);

        Post post = Post.createPost(title, contents, member, category, postTags);
        postRepository.save(post);

        return post;
    }

    public Post updatePost(Long postId, String title, String contents, String memberId, Long categoryId, List<TagDto> tagDtos) {
        Post post = postRepository.findOne(postId);

        Member member = memberService.findOne(memberId);
        Category category = categoryService.findOne(categoryId);
        List<Tag> tags = TagService.bulkSearchAndIfNoneCreate(tagDtos, member);
        List<PostTag> postTags = postTagService.updatePostTag(post,tags);

        post.setTitle(title);
        post.setContent(contents);
        post.setMember(member);
        post.setCategory(category);

        return post;
    }
}
