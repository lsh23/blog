package com.blog.demo.api;

import com.blog.demo.api.dto.comment.CommentDto;
import com.blog.demo.api.dto.comment.CreateCommentRequest;
import com.blog.demo.api.dto.comment.CreateCommentResponse;
import com.blog.demo.domain.Comment;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Post;
import com.blog.demo.service.CommentService;
import com.blog.demo.service.MemberService;
import com.blog.demo.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentApiController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public Result getComments(@RequestParam(value="postId", required = false) Long postId){

        List<Comment> comments;
        if (postId != null){
            comments = commentService.findAllByPostId(postId);
        }
        else{
            comments = commentService.findAll();
        }
        Stream<Comment> commentStream = comments.stream().filter(c -> c.getParent() == null);
        List<CommentDto> commentDtos = commentStream.map(c -> new CommentDto(c)).collect(Collectors.toList());
        Integer totalCommentCount = commentDtos.stream().map(c -> c.getChild().size()).reduce(commentDtos.size(), Integer::sum);

        return new Result(totalCommentCount, commentDtos);
    }

    @PostMapping
    public CreateCommentResponse createComment(@RequestBody @Valid CreateCommentRequest createCommentRequest){

        String memberId = createCommentRequest.getMemberId();
        Member findMember = memberService.findOne(memberId);
        Long postId = createCommentRequest.getPostId();
        Post findPost = postService.findOne(postId);
        String text = createCommentRequest.getText();

        Comment comment = Comment.builder()
                .member(findMember)
                .post(findPost)
                .text(text)
                .build();

        Long parentId = createCommentRequest.getParentId();
        if (parentId != null) {
            Comment findComment = commentService.findOne(parentId);
            comment.assignParent(findComment);
            commentService.save(comment);
            return new CreateCommentResponse(comment.getId(), comment.getPost().getId(), comment.getMember().getId(), comment.getParent().getId());
        }

        commentService.save(comment);
        return new CreateCommentResponse(comment.getId(), comment.getPost().getId(), comment.getMember().getId(), null);
        
    }

    @Data
    @AllArgsConstructor
    class Result<T> {
        private int count;
        private T data;
    }

}
