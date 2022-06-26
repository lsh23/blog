package com.blog.demo.api.dto.comment;

import com.blog.demo.api.CommentApiController;
import com.blog.demo.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private Long memberId;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> child;

    public CommentDto(Long id, String text, Long memberId, Long parentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.text = text;
        this.memberId = memberId;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.memberId = comment.getMember().getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.child = comment.getChild().stream()
                .map(c -> new CommentDto(c.getId(), c.getText(), c.getMember().getId(), c.getParent().getId(), c.getCreatedAt(), c.getUpdatedAt()))
                .collect(Collectors.toList());
    }

}
