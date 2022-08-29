package com.blog.demo.api.dto.auth;

import com.blog.demo.exception.NoSuchOAuthMemberException;
import lombok.Getter;

@Getter
public class OAuthLoginFailErrorResponse{
    private final String email;
    private final String name;
    private String message;

    private OAuthLoginFailErrorResponse(String message, String email, String name) {
        this.message = message;
        this.email = email;
        this.name = name;
    }

    public static OAuthLoginFailErrorResponse from(NoSuchOAuthMemberException exception) {
        String email = exception.getEmail();
        String name = exception.getName();
        return new OAuthLoginFailErrorResponse(exception.getMessage(), email, name);
    }
}