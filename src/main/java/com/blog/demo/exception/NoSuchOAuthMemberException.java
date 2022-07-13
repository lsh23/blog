package com.blog.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchOAuthMemberException extends RuntimeException {
    private static final String MESSAGE = "소셜 로그인 회원이 아닙니다. 회원가입을 진행합니다.";

    private final String email;
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public NoSuchOAuthMemberException(String email) {
        super(MESSAGE);
        this.email = email;
    }
}