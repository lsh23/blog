package com.blog.demo.api.dto.post;

import com.blog.demo.api.dto.tag.TagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {
    @NotEmpty
    private Long memberId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    @NotNull
    private Long categoryId;
    @NotNull
    private List<TagDto> tags;
}
