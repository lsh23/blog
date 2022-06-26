package com.blog.demo.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateLoginRequest {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String password;
}
