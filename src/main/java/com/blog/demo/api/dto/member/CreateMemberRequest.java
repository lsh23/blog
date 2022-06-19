package com.blog.demo.api.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateMemberRequest {
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
}