package com.blog.demo.exception;

import com.blog.demo.api.dto.auth.OAuthLoginFailErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoSuchOAuthMemberException.class)
    public ResponseEntity<OAuthLoginFailErrorResponse> oAuthLoginFailHandler(NoSuchOAuthMemberException exception) {

        return ResponseEntity
                .status(exception.getStatus())
                .body(OAuthLoginFailErrorResponse.from(exception));
    }

    @ExceptionHandler(BlogException.class)
    public ResponseEntity<BlogExceptionDto> blogExceptionHandle(BlogException exception){
        return ResponseEntity.badRequest()
                .body(new BlogExceptionDto(exception.getMessage(),exception.getStatus()));
    }
}
