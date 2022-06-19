package com.blog.demo.api.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateCommentRequest {
    @NotNull
    private Long postId;
    @NotEmpty
    private String memberId;
    @NotEmpty
    private String text;
    private Long parentId;
}
