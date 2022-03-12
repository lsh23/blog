package com.blog.demo.api;

import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Post;
import com.blog.demo.domain.PostTag;
import com.blog.demo.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final PostTagService postTagService;

    @GetMapping("/api/v1/posts")
    public Result getPosts(@RequestParam(value = "id",required = false) String userId){
        List<Post> posts = postService.findPosts();

        if(userId == null){
            List<PostDto> postDtos = posts.stream()
                    .map(p -> new PostDto(p.getId(),p.getTitle(), p.getContent()))
                    .collect(Collectors.toList());
            return new Result(postDtos.size(), postDtos);
        }else{
            Member findMember = memberService.findOne(userId);
            List<PostDto> postDtos = posts.stream()
                    .filter(p -> p.getMember() == findMember)
                    .map(p -> new PostDto(p.getId(),p.getTitle(), p.getContent()))
                    .collect(Collectors.toList());
            return new Result(postDtos.size(), postDtos);
        }
    }

    @PostMapping("/api/v1/posts")
    public CreatePostResponse create(@RequestBody @Valid CreatePostRequest createPostRequest){
        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContents());

        Member findMember = memberService.findOne(createPostRequest.getUserId());
        post.setMember(findMember);

        Category findCategory = categoryService.findOne(createPostRequest.getCategoryId());
        post.setCategory(findCategory);

        if (createPostRequest.getPostTagId() != null) {
            PostTag findPostTag = postTagService.findOne(createPostRequest.getPostTagId());
            post.setPostTag(findPostTag);
        }

        postService.join(post);
        return new CreatePostResponse(post.getId(), post.getTitle());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class PostDto{
        private Long id;
        private String title;
        private String content;
    }

    @Data
    @AllArgsConstructor
    static class CreatePostResponse {
        private Long id;
        private String title;
    }

    @Data
    static class CreatePostRequest {
        private String userId;
        private String title;
        private String contents;
        private Long categoryId;
        private Long postTagId;
    }
}
