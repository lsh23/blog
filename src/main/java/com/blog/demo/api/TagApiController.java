package com.blog.demo.api;

import com.blog.demo.api.dto.Result;
import com.blog.demo.api.dto.tag.*;
import com.blog.demo.service.MemberService;
import com.blog.demo.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagApiController {
    private final TagService tagService;
    private final MemberService memberService;

    @GetMapping
    public Result getPostags(
            @RequestParam(value = "memberId", required = false) String memberId){
        List<TagDto> tagDtos = tagService.findAll(memberId);
        return new Result(tagDtos.size(), tagDtos);
    }

    @PostMapping
    public CreateTagResponse createTag(@RequestBody @Valid CreateTagRequest createTagRequest){
        TagDto tagDto = tagService.createTag(createTagRequest);
        return new CreateTagResponse(tagDto.getId(), tagDto.getName());
    }

    @PatchMapping("/{id}")
    public UpdateTagResponse updateTag(@RequestBody @Valid UpdateTagRequest updateTagRequest, @PathVariable("id") Long id){
        TagDto tagDto = tagService.updateTag(id, updateTagRequest);
        return new UpdateTagResponse(tagDto.getId(), tagDto.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") Long id){
        tagService.deleteOne(id);
        return ResponseEntity.noContent().build();
    }
}

