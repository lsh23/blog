package com.blog.demo.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class CreateLoginResponse {
    private String id;
    private String name;
    private int status;
}
