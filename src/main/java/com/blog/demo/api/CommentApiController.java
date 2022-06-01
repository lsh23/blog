package com.blog.demo.api;

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
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
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
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class CommentDto{

        public CommentDto(Long id, String text, String memberId, Long parentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.text = text;
            this.memberId = memberId;
            this.parentId = parentId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public CommentDto(Comment comment) {
            this.id = comment.getId();
            this.text = comment.getText();
            this.memberId = comment.getMember().getId();
            this.child = comment.getChild().stream()
                    .map(c -> new CommentDto(c.getId(), c.getText(), c.getMember().getId(), c.getParent().getId(), c.getCreatedAt(), c.getUpdatedAt()))
                    .collect(Collectors.toList());
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
        }

        private Long id;
        private String text;
        private String memberId;
        private Long parentId;
        private List<CommentDto> child;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    static class CreateCommentResponse {
        private Long id;
        private Long postId;
        private String memberId;
        private Long parentId;
    }

    @Data
    static class CreateCommentRequest {
        private Long postId;
        private String memberId;
        private String text;
        private Long parentId;
    }
}
