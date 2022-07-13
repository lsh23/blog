package com.blog.demo.auth;

import com.blog.demo.domain.OauthProvider;
import com.blog.demo.domain.oauth.OauthUserInfo;

public interface OauthAPIRequester {
    boolean supports(OauthProvider oauthProvider);
    OauthUserInfo getUserInfoByCode(String code);
}
