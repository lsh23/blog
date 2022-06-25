package com.blog.demo.service;

import com.blog.demo.api.dto.posttag.PostTagDto;
import com.blog.demo.api.dto.tag.TagDto;
import com.blog.demo.domain.Post;
import com.blog.demo.domain.PostTag;
import com.blog.demo.domain.Tag;
import com.blog.demo.repository.PostRepository;
import com.blog.demo.repository.PostTagRepository;
import com.blog.demo.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostTagServiceTest {

    @Autowired
    PostTagRepository postTagRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostTagService postTagService;

    @Test
    @DisplayName("PostTag 단순 저장 - 성공")
    void save_success() {
        // given
        PostTag postTag = PostTag.builder().build();

        // when
        postTagService.save(postTag);

        // then
        assertThat(postTagRepository.findById(postTag.getId()).get()).isEqualTo(postTag);

    }

    @Test
    @DisplayName("PostTag 단순 조회 - 성공")
    void findOneTest_success() {
        // given
        Tag tag = Tag.builder().name("tag").build();
        tagRepository.save(tag);

        PostTag postTag = PostTag.builder().tag(tag).build();
        postTagRepository.save(postTag);

        // when
        PostTagDto findOne = postTagService.findById(postTag.getId());

        // then
        assertThat(findOne.getId()).isEqualTo(postTag.getId());
    }

    @Test
    @DisplayName("PostTag 리스트 조회 - 성공")
    void findPostTagsTest_success() {
        // given
        Post post = Post.builder().build();
        postRepository.save(post);

        Tag tag1 = Tag.builder().name("tag1").build();
        Tag tag2 = Tag.builder().name("tag2").build();
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        PostTag postTag1 = PostTag.builder().tag(tag1).build();
        PostTag postTag2 = PostTag.builder().tag(tag2).build();
        postTag1.assignPost(post);
        postTag2.assignPost(post);

        postTagRepository.save(postTag1);
        postTagRepository.save(postTag2);

        // when
        List<PostTagDto> postTags = postTagService.findPostTags(post.getId());

        // then
        assertThat(postTags.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("PostTag 삭제 - 성공")
    void deleteOneTest_success() {
        // given
        Tag tag = Tag.builder().name("tag").build();
        tagRepository.save(tag);

        PostTag postTag = PostTag.builder().tag(tag).build();
        postTagRepository.save(postTag);

        // when
        postTagService.deleteOne(postTag.getId());

        // then
        assertThat(postTagRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Tag 리스트를 이용한 PostTag 생성 - 성공")
    void saveByTagsTest_success() {
        // given
        Post post = Post.builder().build();
        postRepository.save(post);

        Tag tag1 = Tag.builder().name("tag1").build();
        Tag tag2 = Tag.builder().name("tag2").build();
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(new TagDto(tag1));
        tagDtos.add(new TagDto(tag2));

        // when
        List<PostTagDto> postTagDtos = postTagService.saveByTags(post.getId(), tagDtos);

        // then
        assertThat(postTagDtos.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("PostTag 갱신 - 성공")
    void updatePostTagTest_success() {
        // given
        // given
        Post post = Post.builder().build();
        postRepository.save(post);

        Tag tag1 = Tag.builder().name("tag1").build();
        Tag tag2 = Tag.builder().name("tag2").build();
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(new TagDto(tag1));
        tagDtos.add(new TagDto(tag2));

        List<PostTagDto> postTagDtos = postTagService.saveByTags(post.getId(), tagDtos);

        Tag tag3 = Tag.builder().name("tag3").build();
        tagRepository.save(tag3);

        List<TagDto> newTagDtos = new ArrayList<>();
        newTagDtos.add(new TagDto(tag1));
        newTagDtos.add(new TagDto(tag3));

        // when
        List<PostTagDto> updatedPostTagDtos = postTagService.updatePostTag(post.getId(), newTagDtos);

        // then
        assertThat(updatedPostTagDtos.size()).isEqualTo(2);
        assertThat(updatedPostTagDtos.get(0).getName()).isEqualTo("tag1");
        assertThat(updatedPostTagDtos.get(1).getName()).isEqualTo("tag3");
    }
}