package com.blog.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ExceptionCodeAndDetails {

    NOT_FOUND_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "발생한 에러의 에러코드를 찾을 수 없습니다.", NotFoundErrorCodeException.class),

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND,"해당하는 id의 member를 찾을 수 없습니다.",NotFoundMemberException .class),
    UNSUPPORTED_OAUTH(HttpStatus.BAD_REQUEST,"지원하지 않는 Oauth provider 입니다.",UnsupportedOauthProviderException.class),
    UNABLE_TO_GET_ACCESS_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR,"소셜 로그인에 실패했습니다.",UnableToGetTokenResponseFromGithubException.class),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"로그인이 필요한 서비스입니다.",TokenExpiredException.class),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"로그인이 필요한 서비스입니다.",InvalidTokenException.class),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,"이미 사용 중인 이메일입니다.",DuplicateEmailException.class),
    AUTHORIZATION_HEADER_INVOLVED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다.", AuthorizationHeaderUninvolvedException.class),

    NOT_FOUND_POST(HttpStatus.NOT_FOUND,"해당하는 id의 post를 찾을 수 없습니다.",NotFoundPostException .class),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND,"해당하는 id의 category를 찾을 수 없습니다.",NotFoundCategoryException .class),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND,"해당하는 id의 comment를 찾을 수 없습니다.",NotFoundCategoryException .class),
    NOT_FOUND_TAG(HttpStatus.NOT_FOUND,"해당하는 id의 tag를 찾을 수 없습니다.",NotFoundTagException .class),
    NOT_FOUND_POST_TAG(HttpStatus.NOT_FOUND,"해당하는 id의 posttag를 찾을 수 없습니다.",NotFoundPostTagException .class);


    private final HttpStatus status;
    private final String message;
    private final Class<? extends Exception> type;

    public static ExceptionCodeAndDetails findByClass(Class<? extends Exception> type) {
        return Arrays.stream(ExceptionCodeAndDetails.values())
                .filter(code -> code.type.equals(type))
                .findAny()
                .orElseThrow(NotFoundErrorCodeException::new);
    }
}
