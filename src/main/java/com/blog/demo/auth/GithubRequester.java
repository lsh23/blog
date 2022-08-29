package com.blog.demo.auth;

import com.blog.demo.domain.OauthProvider;
import com.blog.demo.domain.oauth.GithubUserInfo;
import com.blog.demo.domain.oauth.OauthUserInfo;
import com.blog.demo.exception.UnableToGetTokenResponseFromGithubException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@PropertySource("classpath:config/oauth.properties")
public class GithubRequester implements OauthAPIRequester{
    private final String clientId;
    private final String secretId;
    private final WebClient githubOauthLoginClient;
    private final WebClient githubOpenApiClient;

    public GithubRequester(
            @Value("${github.client-id}") final String clientId,
            @Value("${github.secret-id}") final String secretId,
            @Value("${github.uri.oauth-login}") final String githubOauthUrl,
            @Value("${github.uri.open-api}") final String githubOpenApiUrl,
            final WebClient webClient) {

        this.clientId = clientId;
        this.secretId = secretId;
        this.githubOauthLoginClient = githubOauthLoginClient(webClient,githubOauthUrl);
        this.githubOpenApiClient = githubOpenApiClient(webClient, githubOpenApiUrl);
    }

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

    private WebClient githubOauthLoginClient(final WebClient webClient, final String githubOauthUrl) {
        return webClient.mutate()
                .baseUrl(githubOauthUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private WebClient githubOpenApiClient(final WebClient webClient, String githubOpenApiUrl) {
        return webClient.mutate()
                .baseUrl(githubOpenApiUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
