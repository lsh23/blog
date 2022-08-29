package com.blog.demo.api.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class OauthCreateMemberRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String loginId;

    @NotNull
    private String oauthProvider;

    public OauthCreateMemberRequest(final String email,
                                    final String name,
                                    final String loginId,
                                    final String oauthProvider) {
        this.email = email;
        this.oauthProvider = oauthProvider;
    }
}