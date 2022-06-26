package com.blog.demo.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateLoginResponse {
    private Long id;
    private String name;
    private int status;
}
