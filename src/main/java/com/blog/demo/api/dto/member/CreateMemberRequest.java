package com.blog.demo.api.dto.member;

import lombok.Data;

@Data
public class CreateMemberRequest {
    private String id;
    private String name;
    private String password;
}