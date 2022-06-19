package com.blog.demo.api.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMemberRequest {
    @NotEmpty
    private String name;
}
