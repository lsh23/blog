package com.blog.demo.api;

import com.blog.demo.api.dto.Result;
import com.blog.demo.api.dto.tag.TagDto;
import com.blog.demo.api.dto.post.*;
import com.blog.demo.domain.*;
import com.blog.demo.repository.PostSearch;
import com.blog.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;

    @GetMapping
    public Result getPosts(@RequestParam(value = "id",required = false) String userId,
                           @RequestParam(value = "categoryId",required = false) Long categoryId){

        PostSearch postSearch = new PostSearch();
        postSearch.setMemberId(userId);
        postSearch.setCategoryId(categoryId);
        List<Post> posts = postService.findPosts(postSearch);

        Stream<Post> postStream = posts.stream();

        List<PostListDto> postListDtos = postStream
                .map(p -> new PostListDto(p.getId(),p.getTitle(), p.getContent(), p.getCreatedAt(), p.getUpdatedAt(), p.getCategory().getName(), p.getPostTags()))
                .collect(Collectors.toList());

        return new Result(postListDtos.size(), postListDtos);

    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable("id") Long id){
        Post findPost = postService.findOne(id);
        return new PostDto(findPost.getId(), findPost.getTitle(), findPost.getContent(),findPost.getCreatedAt(), findPost.getUpdatedAt(), findPost.getCategory(), findPost.getPostTags());
    }
    
    @PostMapping
    public CreatePostResponse createPost(@RequestBody @Valid CreatePostRequest createPostRequest){
        String title = createPostRequest.getTitle();
        String contents = createPostRequest.getContents();

        String memberId = createPostRequest.getUserId();
        Long categoryId = createPostRequest.getCategoryId();

        List<TagDto> tags = createPostRequest.getTags();

        Post post = postService.writePost(title, contents, memberId, categoryId, tags);

        return new CreatePostResponse(post.getId(), post.getTitle());
    }

    @PatchMapping("/{id}")
    public UpdatePostResponse updatePost(@RequestBody @Valid UpdatePostRequest updatePostRequest, @PathVariable("id") Long id){
        String title = updatePostRequest.getTitle();
        String contents = updatePostRequest.getContents();

        String memberId = updatePostRequest.getUserId();
        Long categoryId = updatePostRequest.getCategoryId();

        List<TagDto> tags = updatePostRequest.getTags();

        Post post = postService.updatePost(id,title, contents, memberId, categoryId, tags);

        return new UpdatePostResponse(post.getId(), post.getTitle());
    }

    @DeleteMapping("/{id}")
    public DeletePostResponse deletePost(@PathVariable("id") Long id){
        Post post = postService.findOne(id);
        postService.deleteOne(id);
        return new DeletePostResponse(post.getId(), post.getTitle());
    }

}
