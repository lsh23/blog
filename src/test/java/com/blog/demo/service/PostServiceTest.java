package com.blog.demo.service;

import com.blog.demo.api.dto.post.CreatePostRequest;
import com.blog.demo.api.dto.post.PostDto;
import com.blog.demo.api.dto.post.PostListDto;
import com.blog.demo.api.dto.post.UpdatePostRequest;
import com.blog.demo.api.dto.tag.TagDto;
import com.blog.demo.domain.Category;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.Post;
import com.blog.demo.domain.Tag;
import com.blog.demo.repository.CategoryRepository;
import com.blog.demo.repository.MemberRepository;
import com.blog.demo.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Post 단순 저장 - 성공")

    void saveTest_success() {
        //given
        Post post = Post.builder()
                .title("title")
                .content("content")
                .build();

        //when
        postService.save(post);

        //then
        assertThat(postRepository.findById(post.getId()).get()).isEqualTo(post);
    }

    @Test
    @DisplayName("Post 단순 조회 - 성공")
    void findOneTest_success() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);
        Category category = Category.builder().member(member).name("category").build();
        categoryRepository.save(category);

        Post post = Post.builder()
                .member(member)
                .category(category)
                .title("title")
                .content("content")
                .build();
        postRepository.save(post);

        //when
        PostDto findOne = postService.findById(post.getId());

        //then
        assertThat(findOne.getId()).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("Post 리스트 조회 - 성공")
    void findPostsTest_success() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);
        Category category = Category.builder().member(member).name("category").build();
        categoryRepository.save(category);

        Post post1 = Post.builder()
                .member(member)
                .category(category)
                .title("title1")
                .content("content2")
                .build();

        postRepository.save(post1);
        //given
        Post post2 = Post.builder()
                .member(member)
                .category(category)
                .title("title2")
                .content("content2")
                .build();
        postRepository.save(post2);

        //when
        List<PostListDto> posts = postService.findPosts(member.getId(),category.getId());

        //then
        assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Post 삭제 - 성공")
    void deleteOneTest_success() {
        //given
        Post post = Post.builder()
                .title("title")
                .content("content")
                .build();
        postRepository.save(post);

        //when
        postRepository.deleteById(post.getId());

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("요청을 통한 Post 생성 - 성공")
    void createPostTest_success() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);
        Category category = Category.builder().member(member).name("category").build();
        categoryRepository.save(category);

        //when

        Tag tag = Tag.builder().name("tag").build();
        TagDto tagDto = new TagDto(tag);
        tagDto.setId(-1L);
        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);

        CreatePostRequest createPostRequest = CreatePostRequest.builder()
                .memberId(member.getId())
                .categoryId(category.getId())
                .contents("content")
                .title("title")
                .tags(tagDtos)
                .build();

        PostDto post = postService.createPost(createPostRequest);

        //then
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getCategory().getName()).isEqualTo("category");
        assertThat(post.getPostTags().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("요청을 통한 Post 수정 - 성공")
    void updatePostTest_success() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);
        Category category = Category.builder().member(member).name("category").build();
        categoryRepository.save(category);

        Tag tag = Tag.builder().name("tag").build();
        TagDto tagDto = new TagDto(tag);
        tagDto.setId(-1L);
        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);

        CreatePostRequest createPostRequest = CreatePostRequest.builder()
                .memberId(member.getId())
                .categoryId(category.getId())
                .contents("content")
                .title("title")
                .tags(tagDtos)
                .build();

        createPostRequest.setTags(tagDtos);

        PostDto post = postService.createPost(createPostRequest);

        //when
        Category category2 = Category.builder().member(member).name("category2").build();
        categoryRepository.save(category2);

        Tag tag1 = Tag.builder().name("tag1").build();
        TagDto tag1Dto = new TagDto(tag1);
        tag1Dto.setId(-1L);
        Tag tag2 = Tag.builder().name("tag2").build();
        TagDto tag2Dto = new TagDto(tag2);
        tag2Dto.setId(-1L);
        List<TagDto> newTagDtos = new ArrayList<>();
        newTagDtos.add(tag1Dto);
        newTagDtos.add(tag2Dto);

        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                .memberId(member.getId())
                .categoryId(category2.getId())
                .contents("content2")
                .title("title2")
                .tags(newTagDtos)
                .build();

        PostDto postDto = postService.updatePost(post.getId(), updatePostRequest);

        //then
        Post updated = postRepository.findById(post.getId()).get();
        assertThat(updated.getTitle()).isEqualTo("title2");
        assertThat(updated.getContent()).isEqualTo("content2");
        assertThat(updated.getCategory().getName()).isEqualTo("category2");
        assertThat(updated.getPostTags().size()).isEqualTo(2);
    }
}