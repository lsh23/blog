package com.blog.demo.api.dto.auth;

import com.blog.demo.exception.NoSuchOAuthMemberException;
import lombok.Getter;

@Getter
public class OAuthLoginFailErrorResponse{
    private final String email;
    private String message;

    private OAuthLoginFailErrorResponse(String message, String email) {
        this.message = message;
        this.email = email;
    }

    public static OAuthLoginFailErrorResponse from(NoSuchOAuthMemberException exception) {
        String email = exception.getEmail();
        return new OAuthLoginFailErrorResponse(exception.getMessage(), email);
    }
}