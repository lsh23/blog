package com.blog.demo.api.dto.member;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UpdateMemberRequest {
    @NotEmpty
    private String name;
}
