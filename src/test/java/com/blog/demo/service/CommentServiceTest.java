package com.blog.demo.service;

import com.blog.demo.api.dto.comment.CommentDto;
import com.blog.demo.api.dto.comment.CreateCommentRequest;
import com.blog.demo.domain.Comment;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Post;
import com.blog.demo.repository.CommentRepository;
import com.blog.demo.repository.MemberRepository;
import com.blog.demo.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;
    @Test
    @DisplayName("comment 저장")
    void saveTest() {
        //given
        Comment comment = Comment.builder().text("text").build();

        //when
        Long commentId = commentService.save(comment);

        //then
        assertThat(commentService.findOne(commentId)).isEqualTo(comment);
    }


    @Test
    @DisplayName("comment 단일 조회")
    void findOneTest() {
        //given
        Comment comment = Comment.builder().text("text").build();
        Long commentId = commentService.save(comment);

        //when
        String text = commentService.findOne(commentId).getText();

        //then
        assertThat(text).isEqualTo("text");
    }

    @Test
    @DisplayName("comment 리스트 조회")
    void findAllTest() {
        //given
        Member member = Member.builder().id("member").build();
        memberRepository.save(member);

        Arrays.asList("text1", "text2", "text3")
                .stream()
                .map(text -> Comment.builder().text(text).member(member).build())
                .forEach(comment -> commentService.save(comment));

        //when
        List<CommentDto> all = commentService.findAllRootComment(null);

        //then
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("comment 리스트 조회 by post")
    void findAllByPostIdTest() {
        //given
        Member member = Member.builder().id("member").build();
        memberRepository.save(member);

        Post post = Post.builder().title("title").build();
        postRepository.save(post);

        Comment comment1 = Comment.builder().text("text1").member(member).post(post).build();
        commentRepository.save(comment1);
        Comment comment2 = Comment.builder().text("text2").member(member).post(post).build();
        commentRepository.save(comment2);

        //when
        List<CommentDto> comments = commentService.findAllRootComment(post.getId());

        //then
        assertThat(comments.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("createComment 저장")
    void createCommentTest() {
        //given
        Member member = Member.builder().id("member").build();
        memberRepository.save(member);

        Post post = Post.builder().title("title").build();
        postRepository.save(post);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setMemberId(member.getId());
        createCommentRequest.setPostId(post.getId());
        createCommentRequest.setText("text");

        //when
        CommentDto comment = commentService.createComment(createCommentRequest);
        Long commentId = comment.getId();

        //then
        Comment findOne = commentRepository.findOne(commentId);
        assertThat(findOne.getMember().getId()).isEqualTo("member");
        assertThat(findOne.getPost().getTitle()).isEqualTo("title");
        assertThat(findOne.getText()).isEqualTo("text");
    }

}