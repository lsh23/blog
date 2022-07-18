package com.blog.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlogExceptionDto extends RuntimeException {

    private HttpStatus status;
    private String message;

    public BlogExceptionDto(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BlogExceptionDto(final String message, final Throwable cause, final HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
