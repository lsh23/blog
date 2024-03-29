package com.blog.demo.api.dto.post;

import com.blog.demo.api.dto.tag.TagDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class UpdatePostRequest {
    @NotEmpty
    private String memberId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    @NotNull
    private Long categoryId;
    @NotNull
    private List<TagDto> tags;
}
