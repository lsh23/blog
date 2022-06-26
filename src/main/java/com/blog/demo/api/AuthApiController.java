package com.blog.demo.api;

import com.blog.demo.api.dto.auth.CreateLoginRequest;
import com.blog.demo.api.dto.auth.CreateLoginResponse;
import com.blog.demo.domain.Member;
import com.blog.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthApiController {
    private final MemberService memberService;

    @PostMapping("/api/v1/auth/login")
    public CreateLoginResponse login(@RequestBody @Valid CreateLoginRequest createLoginRequest){
        Member findMember = memberService.findById(createLoginRequest.getId());

        if (findMember.getPassword().equals(createLoginRequest.getPassword())){
            return new CreateLoginResponse(findMember.getId(), findMember.getName(),200);
        }

        return new CreateLoginResponse(findMember.getId(), findMember.getName(),404);
    }
}
