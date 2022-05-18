package com.blog.demo.api;

import com.blog.demo.domain.*;
import com.blog.demo.repository.PostSearch;
import com.blog.demo.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;
    private final PostTagService postTagService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TagService tagService;

    @GetMapping
    public Result getPosts(@RequestParam(value = "id",required = false) String userId,
                           @RequestParam(value = "categoryId",required = false) Long categoryId){

        PostSearch postSearch = new PostSearch();
        postSearch.setMemberId(userId);
        postSearch.setCategoryId(categoryId);
        List<Post> posts = postService.findPosts(postSearch);

        Stream<Post> postStream = posts.stream();

        List<PostDto> postDtos = postStream
                .map(p -> new PostDto(p.getId(),p.getTitle(), p.getContent(), p.getCreatedAt(), p.getUpdatedAt()))
                .collect(Collectors.toList());

        return new Result(postDtos.size(), postDtos);

    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable("id") Long id){
        Post findPost = postService.findOne(id);
        return new PostDto(findPost.getId(), findPost.getTitle(), findPost.getContent(),findPost.getCreatedAt(), findPost.getUpdatedAt(), findPost.getPostTags());
    }
    
    @PostMapping
    public CreatePostResponse createPost(@RequestBody @Valid CreatePostRequest createPostRequest){
        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContents());

        Member findMember = memberService.findOne(createPostRequest.getUserId());
        post.setMember(findMember);

        Category findCategory = categoryService.findOne(createPostRequest.getCategoryId());
        post.setCategory(findCategory);

        postService.join(post);

        if (createPostRequest.getTagIds() != null) {
            List<Long> tagIds = createPostRequest.getTagIds();
            for (Long tagId: tagIds) {
                Tag findTag = tagService.findOne(tagId);
                PostTag postTag = new PostTag();
                postTag.setPost(post);
                postTag.setTag(findTag);
                postTagService.join(postTag);
            }

        }
        return new CreatePostResponse(post.getId(), post.getTitle());
    }

    @PatchMapping("/{id}")
    public UpdatePostResponse updatePost(@RequestBody @Valid UpdatePostRequest updatePostRequest, @PathVariable("id") Long id){
        Post post = postService.findOne(id);

        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContents());

        Member findMember = memberService.findOne(updatePostRequest.getUserId());
        post.setMember(findMember);

        Category findCategory = categoryService.findOne(updatePostRequest.getCategoryId());
        post.setCategory(findCategory);

        if (updatePostRequest.getTagIds() != null) {

            List<PostTag> postTagsByPostId = postTagService.findPostTagsByPostId(id);
            for (PostTag postTag: postTagsByPostId) {
                postTagService.deleteOne(postTag.getId());
            }

            List<Long> tagIds = updatePostRequest.getTagIds();
            for (Long tagId: tagIds) {
                Tag findTag = tagService.findOne(tagId);
                PostTag postTag = new PostTag();
                postTag.setTag(findTag);
                postTag.setPost(post);
                postTagService.join(postTag);
            }
        }

        return new UpdatePostResponse(post.getId(), post.getTitle());
    }

    @DeleteMapping("/{id}")
    public DeletePostResponse deletePost(@PathVariable("id") Long id){
        Post post = postService.findOne(id);
        postService.deleteOne(id);
        return new DeletePostResponse(post.getId(), post.getTitle());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    static class PostDto{
        private Long id;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private List<PostTagDto> postTags;

        public PostDto(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }

        public PostDto(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<PostTag> postTags) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
            this.postTags = postTags.stream().map(pt->new PostTagDto(pt)).collect(Collectors.toList());
        }
    }

    @Data
    static class PostTagDto{

        private Long id;
        private String name;

        public PostTagDto(PostTag postTag){
            this.id = postTag.getId();
            this.name = postTag.getTag().getName();
        }

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
        private List<Long> tagIds = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    static class UpdatePostResponse {
        private Long id;
        private String title;
    }

    @Data
    static class UpdatePostRequest {
        private String userId;
        private String title;
        private String contents;
        private Long categoryId;
        private List<Long> tagIds = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    static class DeletePostResponse {
        private Long id;
        private String title;
    }
}
