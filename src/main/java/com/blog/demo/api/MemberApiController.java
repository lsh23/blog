package com.blog.demo.api;

import com.blog.demo.api.dto.Result;
import com.blog.demo.api.dto.member.*;
import com.blog.demo.domain.Member;
import com.blog.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;


    @GetMapping
    public Result getMembers() {
        List<MemberDto> memberDtos = memberService.findAll();
        return new Result(memberDtos.size(), memberDtos);
    }

    @PostMapping
    public ResponseEntity<CreateMemberResponse> createMember(@RequestBody @Valid CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @PutMapping("/{id}")
    public UpdateMemberResponse updateMember(
            @PathVariable("id") String memberId,
            @RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
        MemberDto member = memberService.updateMember(memberId, updateMemberRequest);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }


}
