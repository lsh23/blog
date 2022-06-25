package com.blog.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ExceptionCodeAndDetails {

    NOT_FOUND_ERROR_CODE("0001", "발생한 에러의 에러코드를 찾을 수 없습니다.", NotFoundErrorCodeException.class),

    NOT_FOUND_MEMBER("1001","해당하는 id의 post를 찾을 수 없습니다.",NotFoundMemberException .class),

    NOT_FOUND_POST("2001","해당하는 id의 post를 찾을 수 없습니다.",NotFoundPostException .class),
    NOT_FOUND_CATEGORY("2002","해당하는 id의 category를 찾을 수 없습니다.",NotFoundCategoryException .class),
    NOT_FOUND_COMMENT("2003","해당하는 id의 comment를 찾을 수 없습니다.",NotFoundCategoryException .class),
    NOT_FOUND_TAG("2004","해당하는 id의 tag를 찾을 수 없습니다.",NotFoundTagException .class),
    NOT_FOUND_POST_TAG("2005","해당하는 id의 posttag를 찾을 수 없습니다.",NotFoundPostTagException .class);


    private final String code;
    private final String message;
    private final Class<? extends Exception> type;

    public static ExceptionCodeAndDetails findByClass(Class<? extends Exception> type) {
        return Arrays.stream(ExceptionCodeAndDetails.values())
                .filter(code -> code.type.equals(type))
                .findAny()
                .orElseThrow(NotFoundErrorCodeException::new);
    }
}
