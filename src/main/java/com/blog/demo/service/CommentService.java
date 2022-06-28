package com.blog.demo.service;

import com.blog.demo.api.dto.comment.CommentDto;
import com.blog.demo.api.dto.comment.CreateCommentRequest;
import com.blog.demo.domain.Comment;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Post;
import com.blog.demo.exception.NotFoundCommentException;
import com.blog.demo.exception.NotFoundMemberException;
import com.blog.demo.exception.NotFoundPostException;
import com.blog.demo.repository.CommentRepository;
import com.blog.demo.repository.MemberRepository;
import com.blog.demo.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    public Long save(Comment comment){
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public Comment findById(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
        return comment;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllRootComment(Long postId) {
        return getAllRootComment(postId).stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }

    private List<Comment> getAllRootComment(Long postId){

        if (postId == null) {
            return commentRepository.findAllRootComment();
        }
        return commentRepository.findAllRootCommentByPostId(postId);
    }

    public CommentDto createComment(CreateCommentRequest createCommentRequest) {
        Long memberId = createCommentRequest.getMemberId();
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        Long postId = createCommentRequest.getPostId();
        Post findPost = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);
        String text = createCommentRequest.getText();

        Comment comment = Comment.builder()
                .member(findMember)
                .post(findPost)
                .text(text)
                .build();

        Long parentId = createCommentRequest.getParentId();

        if (parentId != null) {
            Comment findComment = commentRepository.findById(parentId)
                    .orElseThrow(NotFoundCommentException::new);
            comment.assignParent(findComment);
        }

        commentRepository.save(comment);
        return new CommentDto(comment);
    }
}
