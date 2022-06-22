package com.blog.demo.api.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class UpdateTagRequest {
    @NotEmpty
    String memberId;
    @NotEmpty
    String name;
}
