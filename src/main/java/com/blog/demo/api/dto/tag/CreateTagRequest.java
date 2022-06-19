package com.blog.demo.api.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class CreateTagRequest {
    @NotEmpty
    String memberId;
    @NotEmpty
    String name;
}
