package com.byllameister.modelstore.products.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ProductCommentDto {
    private Long id;
    private Long productId;
    private CommentUserDto user;
    private String comment;
    private Long likes;
    private Instant createdAt;
}
