package com.blog.demo.api.dto.comment;

import lombok.Data;

@Data
public
class CreateCommentRequest {
    private Long postId;
    private String memberId;
    private String text;
    private Long parentId;
}
