package com.blog.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlogExceptionDto extends RuntimeException {

    private String code;
    private String message;

}
