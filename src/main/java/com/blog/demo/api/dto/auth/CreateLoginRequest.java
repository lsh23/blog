package com.blog.demo.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateLoginRequest {
    CreateLoginRequest() {}
    private String id;
    private String password;
}
