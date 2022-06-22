package com.blog.demo.api.dto.member;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CreateMemberRequest {
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
}