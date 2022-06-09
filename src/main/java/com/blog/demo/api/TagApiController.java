package com.blog.demo.api;

import com.blog.demo.domain.Member;
import com.blog.demo.domain.Tag;
import com.blog.demo.service.MemberService;
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
@RequestMapping("/api/v1/tags")
public class TagApiController {
    private final TagService tagService;
    private final MemberService memberService;

    @GetMapping
    public Result getPostags(
            @RequestParam(value = "memberId", required = false) String memberId){

        Stream<Tag> tagStream;

        if (memberId != null){
            tagStream = tagService.findAllByMemberId(memberId).stream();
        }
        else{
            tagStream = tagService.findAll().stream();
        }

        List<TagDto> tagDtos = tagStream.map(t -> new TagDto(t.getId(), t.getName())).collect(Collectors.toList());
        return new Result(tagDtos.size(), tagDtos);
    }

    @PostMapping
    public CreateTagResponse createTag(@RequestBody @Valid CreateTagRequest createTagRequest){

        String name = createTagRequest.getName();
        String memberId = createTagRequest.getMemberId();
        Member findMember = memberService.findOne(memberId);
        Tag tag = Tag.builder()
                .name(name)
                .member(findMember)
                .build();
        tagService.save(tag);
        return new CreateTagResponse(tag.getId(), tag.getName());
    }

    @PatchMapping("/{id}")
    public UpdateTagResponse updateTag(@RequestBody @Valid UpdateTagRequest updateTagRequest, @PathVariable("id") Long id){
        Tag tag = tagService.findOne(id);
        String name = updateTagRequest.getName();
        tag.updateName(name);
        return new UpdateTagResponse(tag.getId(), tag.getName());
    }

    @DeleteMapping("/{id}")
    public DeleteTagResponse deleteTag(@PathVariable("id") Long id){
        Tag tag = tagService.findOne(id);
        tagService.deleteOne(id);
        return new DeleteTagResponse(tag.getId(), tag.getName());
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

    @Data
    @AllArgsConstructor
    static class UpdateTagResponse {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateTagRequest {
        String memberId;
        String name;
    }

    @Data
    @AllArgsConstructor
    static class DeleteTagResponse {
        private Long id;
        private String name;
    }
}

