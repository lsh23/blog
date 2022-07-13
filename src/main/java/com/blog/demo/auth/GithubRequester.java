package com.blog.demo.auth;

import com.blog.demo.domain.OauthProvider;
import com.blog.demo.domain.oauth.GithubUserInfo;
import com.blog.demo.domain.oauth.OauthUserInfo;
import com.blog.demo.exception.UnableToGetTokenResponseFromGithubException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class GithubRequester implements OauthAPIRequester{
    private final String clientId = "9a10bd63b3e75dab97d5" ;
    private final String secretId = "eb44c2a2ee7e287c86944fd41ab3f559d9ebd290";
    private final WebClient githubOauthLoginClient = WebClient.create().mutate()
            .baseUrl("https://github.com/login/oauth/")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();;

    private final WebClient githubOpenApiClient = WebClient.create().mutate()
            .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();


    @Override
    public boolean supports(OauthProvider oauthProvider) {
            return oauthProvider.isSameAs(OauthProvider.GITHUB);
    }

    @Override
    public OauthUserInfo getUserInfoByCode(final String code) {
        String token = getToken(code);
        return getUserInfo(token);
    }

    public String getToken(String code) {
        Map<String, Object> responseBody = githubOauthLoginClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/access_token")
                        .queryParam("code", code)
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", secretId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .blockOptional()
                .orElseThrow(UnableToGetTokenResponseFromGithubException::new);
        validateResponseBody(responseBody);
        return responseBody.get("access_token").toString();
    }

    private OauthUserInfo getUserInfo(final String token) {
        Map<String, Object> responseBody = githubOpenApiClient
                .get()
                .uri("/user")
                .header(HttpHeaders.AUTHORIZATION, "token " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .blockOptional()
                .orElseThrow(UnableToGetTokenResponseFromGithubException::new);
        return GithubUserInfo.from(responseBody);
    }

    private void validateResponseBody(Map<String, Object> responseBody) {
        if (!responseBody.containsKey("access_token")) {
            throw new UnableToGetTokenResponseFromGithubException();
        }
    }
}
