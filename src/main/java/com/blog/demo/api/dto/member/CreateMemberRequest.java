package com.blog.demo.api.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberRequest {
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
}