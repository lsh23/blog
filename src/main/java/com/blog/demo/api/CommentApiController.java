package com.blog.demo.api;

import com.blog.demo.api.dto.Result;
import com.blog.demo.api.dto.comment.CommentDto;
import com.blog.demo.api.dto.comment.CreateCommentRequest;
import com.blog.demo.api.dto.comment.CreateCommentResponse;
import com.blog.demo.service.CommentService;
import com.blog.demo.service.MemberService;
import com.blog.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentApiController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public Result getComments(@RequestParam(value="postId", required = false) Long postId){
        List<CommentDto> commentDtos = commentService.findAll(postId);
        Integer totalCommentCount = commentDtos.stream().map(c -> c.getChild().size()).reduce(commentDtos.size(), Integer::sum);
        return new Result(totalCommentCount, commentDtos);
    }

    @PostMapping
    public CreateCommentResponse createComment(@RequestBody @Valid CreateCommentRequest createCommentRequest){
        CommentDto commentDto = commentService.createComment(createCommentRequest);
        return new CreateCommentResponse(commentDto.getId(), commentDto.getMemberId(), null);
    }

}
