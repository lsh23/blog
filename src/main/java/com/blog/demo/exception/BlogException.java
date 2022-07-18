package com.blog.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BlogException extends RuntimeException {

    private final ExceptionCodeAndDetails codeAndMessage = ExceptionCodeAndDetails
            .findByClass(this.getClass());


    private String message;
    private HttpStatus status;

    public BlogException()  {
        this.message = codeAndMessage.getMessage();
        this.status = codeAndMessage.getStatus();
    }

    public BlogException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
