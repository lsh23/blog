package com.blog.demo.auth;

import com.blog.demo.domain.OauthProvider;
import com.blog.demo.domain.oauth.OauthUserInfo;
import com.blog.demo.exception.UnsupportedOauthProviderException;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class OauthHandler {
    private final List<OauthAPIRequester> oauthAPIRequesters;

    public OauthHandler(final List<OauthAPIRequester> oauthAPIRequesters) {
        this.oauthAPIRequesters = oauthAPIRequesters;
    }

    public OauthUserInfo getUserInfoFromCode(final OauthProvider oauthProvider, final String code) {
        OauthAPIRequester requester = getRequester(oauthProvider);
        return requester.getUserInfoByCode(code);
    }

    private OauthAPIRequester getRequester(final OauthProvider oauthProvider) {
        return oauthAPIRequesters.stream()
                .filter(requester -> requester.supports(oauthProvider))
                .findFirst()
                .orElseThrow(UnsupportedOauthProviderException::new);
    }
}
