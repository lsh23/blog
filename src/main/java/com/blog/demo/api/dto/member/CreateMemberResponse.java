package com.blog.demo.api.dto.member;

import com.blog.demo.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CreateMemberResponse {
    private Long id;
    
    private CreateMemberResponse( Long id) {
        this.id = id;
    }
    
    public static CreateMemberResponse from(Member member) {
        return new CreateMemberResponse(member.getId());
    }
}