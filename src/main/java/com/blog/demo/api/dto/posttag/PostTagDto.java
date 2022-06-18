package com.blog.demo.api.dto.posttag;

import com.blog.demo.domain.PostTag;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostTagDto {

    private Long id;
    private String name;

    public PostTagDto(PostTag postTag) {
        this.id = postTag.getId();
        this.name = postTag.getTag().getName();
    }

    public static List<PostTagDto> toList(List<PostTag> postTags){
        return postTags.stream().map(pt -> new PostTagDto(pt)).collect(Collectors.toList());
    }

}
