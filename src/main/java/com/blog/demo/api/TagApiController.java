package com.blog.demo.api;

import com.blog.demo.domain.Member;
import com.blog.demo.domain.PostTag;
import com.blog.demo.domain.Tag;
import com.blog.demo.service.MemberService;
import com.blog.demo.service.PostService;
import com.blog.demo.service.TagService;
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
public class TagApiController {
    private final TagService TagService;
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/api/v1/tags")
    public Result getPostags(
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "postId", required = false) Long postId){

        Stream<Tag> tagStream = TagService.findTags().stream();

        if (memberId != null){
            tagStream = tagStream.filter(t -> t.getMember().getId().equals(memberId));
        }

        if (postId != null){
            tagStream = tagStream.filter(t -> t.getPostTags().stream()
                                    .filter(pt -> pt.getPost().getId() == postId)
                                    .collect(Collectors.toList()).size() > 0
            );
        }

        List<TagDto> tagDtos = tagStream.map(t -> new TagDto(t.getId(), t.getName())).collect(Collectors.toList());
        return new Result(tagDtos.size(), tagDtos);
    }

    @PostMapping("/api/v1/tags")
    public CreateTagResponse createTag(@RequestBody @Valid CreateTagRequest createTagRequest){
        Tag tag = new Tag();
        String name = createTagRequest.getName();
        String memberId = createTagRequest.getMemberId();
        Member findMember = memberService.findOne(memberId);
        tag.setName(name);
        tag.setMember(findMember);
        TagService.join(tag);
        return new CreateTagResponse(tag.getId(), tag.getName());
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class TagDto{
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateTagResponse {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateTagRequest{
        String memberId;
        String name;
    }

}

