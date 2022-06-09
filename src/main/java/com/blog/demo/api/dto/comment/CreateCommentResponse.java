package com.blog.demo.api.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class CreateCommentResponse {
    private Long id;
    private Long postId;
    private String memberId;
    private Long parentId;
}
