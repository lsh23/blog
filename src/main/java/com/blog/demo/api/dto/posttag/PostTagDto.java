package com.blog.demo.api.dto.posttag;

import com.blog.demo.domain.PostTag;
import lombok.Data;

@Data
public
class PostTagDto {

    private Long id;
    private String name;

    public PostTagDto(PostTag postTag) {
        this.id = postTag.getId();
        this.name = postTag.getTag().getName();
    }

}
