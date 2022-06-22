package com.blog.demo.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
public class CreateLoginRequest {
    CreateLoginRequest() {}
    @NotEmpty
    private String id;
    @NotEmpty
    private String password;
}
