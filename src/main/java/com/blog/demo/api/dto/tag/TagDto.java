package com.blog.demo.api.dto.tag;

import com.blog.demo.domain.Tag;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class TagDto {
    private Long id;
    private String name;

    public TagDto(){}

    public TagDto(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
