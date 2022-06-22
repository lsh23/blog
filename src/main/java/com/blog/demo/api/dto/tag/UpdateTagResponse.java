package com.blog.demo.api.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTagResponse {
    private Long id;
    private String name;
}
