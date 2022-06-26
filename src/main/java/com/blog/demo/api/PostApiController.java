package com.blog.demo.api;

import com.blog.demo.api.dto.Result;
import com.blog.demo.api.dto.post.*;
import com.blog.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;

    @GetMapping
    public Result getPosts(@RequestParam(value = "memberId",required = false) String memberId,
                           @RequestParam(value = "categoryId",required = false) Long categoryId){
        List<PostListDto> postListDtos = postService.findPosts(memberId, categoryId);
        return new Result(postListDtos.size(), postListDtos);
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable("id") Long id){
        PostDto postDto = postService.findById(id);
        return postDto;
    }
    
    @PostMapping
    public CreatePostResponse createPost(@RequestBody @Valid CreatePostRequest createPostRequest){
        PostDto postDto = postService.createPost(createPostRequest);
        return new CreatePostResponse(postDto.getId(), postDto.getTitle());
    }

    @PatchMapping("/{id}")
    public UpdatePostResponse updatePost(@RequestBody @Valid UpdatePostRequest updatePostRequest, @PathVariable("id") Long id){
        PostDto postDto = postService.updatePost(id,updatePostRequest);
        return new UpdatePostResponse(postDto.getId(), postDto.getTitle());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id){
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
