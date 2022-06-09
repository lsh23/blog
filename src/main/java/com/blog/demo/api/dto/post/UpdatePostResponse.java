package com.blog.demo.api.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class UpdatePostResponse {
    private Long id;
    private String title;
}
