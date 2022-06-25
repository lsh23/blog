package com.blog.demo.exception;

import lombok.Getter;

@Getter
public class BlogException extends RuntimeException {

    private final ExceptionCodeAndDetails codeAndMessage = ExceptionCodeAndDetails
            .findByClass(this.getClass());

    private String code;
    private String message;

    public BlogException()  {
        this.code = codeAndMessage.getCode();
        this.message = codeAndMessage.getMessage();
    }
}
