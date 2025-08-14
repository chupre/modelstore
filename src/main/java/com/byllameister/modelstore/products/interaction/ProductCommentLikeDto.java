package com.byllameister.modelstore.products.interaction;

import lombok.Data;

import java.time.Instant;

@Data
public class ProductCommentLikeDto {
    private Long id;
    private Long commentId;
    private Long userId;
    private Instant createdAt;
}
