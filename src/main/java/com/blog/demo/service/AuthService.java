package com.blog.demo.service;

import com.blog.demo.api.dto.auth.TokenResponse;
import com.blog.demo.auth.JwtUtils;
import com.blog.demo.auth.OauthHandler;
import com.blog.demo.domain.OauthProvider;
import com.blog.demo.domain.Member;
import com.blog.demo.domain.oauth.OauthUserInfo;
import com.blog.demo.exception.NoSuchOAuthMemberException;
import com.blog.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

//
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final OauthHandler oauthHandler;

    public AuthService(MemberRepository memberRepository, JwtUtils jwtUtils, OauthHandler oauthHandler) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
        this.oauthHandler = oauthHandler;
    }

    public TokenResponse loginByOauth(final OauthProvider oauthProvider, final String code) {
        OauthUserInfo userInfoFromCode = oauthHandler.getUserInfoFromCode(oauthProvider, code);
        String email = userInfoFromCode.getEmail();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchOAuthMemberException(email));

        String token = issueToken(member);
        return TokenResponse.from(token);
    }

    private String issueToken(final Member findMember) {
        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail(findMember.getEmail())
                .setUserLoginId(findMember.getLoginId())
                .setUserId(findMember.getId())
                .build();

        return jwtUtils.createToken(payload);
    }
}
