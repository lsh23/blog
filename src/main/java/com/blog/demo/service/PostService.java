package com.blog.demo.service;

import com.blog.demo.api.dto.post.CreatePostRequest;
import com.blog.demo.api.dto.post.PostDto;
import com.blog.demo.api.dto.post.PostListDto;
import com.blog.demo.api.dto.post.UpdatePostRequest;
import com.blog.demo.api.dto.posttag.PostTagDto;
import com.blog.demo.api.dto.tag.TagDto;
import com.blog.demo.domain.*;
import com.blog.demo.exception.NotFoundCategoryException;
import com.blog.demo.exception.NotFoundMemberException;
import com.blog.demo.exception.NotFoundPostException;
import com.blog.demo.repository.CategoryRepository;
import com.blog.demo.repository.MemberRepository;
import com.blog.demo.repository.PostRepository;
import com.blog.demo.repository.PostSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagService tagService;
    private final PostTagService postTagService;

    public PostService(PostRepository postRepository, MemberRepository memberRepository, CategoryRepository categoryRepository, com.blog.demo.service.TagService tagService, PostTagService postTagService) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.tagService = tagService;
        this.postTagService = postTagService;
    }

    public Long save(Post post){
        postRepository.save(post);
        return post.getId();
    }

    public PostDto findOne(long id) {
        Post findOne = postRepository.findById(id)
                .orElseThrow(()-> new NotFoundPostException());
        if (findOne == null){
            throw new NotFoundPostException();
        }
        return new PostDto(findOne);
    }

    @Transactional(readOnly = true)
    public List<PostListDto> findPosts(String memberId, Long categoryId) {
        PostSearch postSearch = new PostSearch();
        postSearch.setMemberId(memberId);
        postSearch.setCategoryId(categoryId);
        return postRepository.findPosts(postSearch).stream()
                .map(PostListDto::new)
                .collect(Collectors.toList());
    }

    public void deleteOne(long id) {
        postRepository.deleteById(id);
    }

    public PostDto createPost(CreatePostRequest createPostRequest) {

        String title = createPostRequest.getTitle();
        String contents = createPostRequest.getContents();

        String memberId = createPostRequest.getMemberId();
        Long categoryId = createPostRequest.getCategoryId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new NotFoundMemberException());

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new NotFoundCategoryException());

        Post post = Post.builder()
                .title(title)
                .content(contents)
                .member(member)
                .category(category)
                .build();

        postRepository.save(post);

        List<TagDto> tagDtos = createPostRequest.getTags();
        List<TagDto> resultTagDtos = tagService.bulkSearchAndIfNoneCreate(memberId, tagDtos);
        List<PostTagDto> postTags = postTagService.saveByTags(post.getId() ,resultTagDtos);



        return new PostDto(post);
    }

    public PostDto updatePost(Long postId, UpdatePostRequest updatePostRequest) {
        String title = updatePostRequest.getTitle();
        String contents = updatePostRequest.getContents();

        String memberId = updatePostRequest.getMemberId();
        Long categoryId = updatePostRequest.getCategoryId();

        List<TagDto> tagDtos = updatePostRequest.getTags();

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new NotFoundPostException());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new NotFoundMemberException());

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundCategoryException());

        post.updateAll(title, contents, member, category);

        List<TagDto> resultTagDtos = tagService.bulkSearchAndIfNoneCreate(memberId, tagDtos);
        List<PostTagDto> postTags = postTagService.updatePostTag(postId,resultTagDtos);

        return new PostDto(post);
    }
}
