package com.blog.demo.api.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTagRequest {
    @NotEmpty
    Long memberId;
    @NotEmpty
    String name;
}
