package com.blog.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BlogException.class)
    public ResponseEntity<BlogExceptionDto> blogExceptionHandle(BlogException exception){
        return ResponseEntity.badRequest()
                .body(new BlogExceptionDto(exception.getCode(), exception.getMessage()));
    }
}
