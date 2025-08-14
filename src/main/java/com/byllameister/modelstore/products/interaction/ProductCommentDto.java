package com.byllameister.modelstore.products.interaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ProductCommentDto {
    private Long id;
    private Long productId;
    private Long userId;
    private String comment;
    private Long likes;
    private Instant createdAt;
}
