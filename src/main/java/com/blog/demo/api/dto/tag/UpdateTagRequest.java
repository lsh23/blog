package com.blog.demo.api.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateTagRequest {
    @NotEmpty
    String memberId;
    @NotEmpty
    String name;
}
