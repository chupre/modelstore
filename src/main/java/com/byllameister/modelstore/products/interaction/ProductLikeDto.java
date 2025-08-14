package com.byllameister.modelstore.products.interaction;

import lombok.Data;

import java.time.Instant;

@Data
public class ProductLikeDto {
    private Long id;
    private Long productId;
    private Long userId;
    private Instant createdAt;
}
