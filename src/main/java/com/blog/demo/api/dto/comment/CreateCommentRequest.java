package com.blog.demo.api.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    @NotNull
    private Long postId;
    @NotEmpty
    private String memberId;
    @NotEmpty
    private String text;
    private Long parentId;
}
