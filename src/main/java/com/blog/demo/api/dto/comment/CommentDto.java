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

    public CommentDto(Long id, String text, String memberId, Long parentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.child = comment.getChild().stream()
                .map(c -> new CommentDto(c.getId(), c.getText(), c.getMember().getId(), c.getParent().getId(), c.getCreatedAt(), c.getUpdatedAt()))
                .collect(Collectors.toList());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    private Long id;
    private String text;
    private String memberId;
    private Long parentId;
    private List<CommentDto> child;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
