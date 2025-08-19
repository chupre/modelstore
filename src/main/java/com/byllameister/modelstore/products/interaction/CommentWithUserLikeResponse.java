package com.byllameister.modelstore.products.interaction;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CommentWithUserLikeResponse extends ProductCommentDto{
    Boolean isLiked;

    public CommentWithUserLikeResponse(
            Long id,
            Long productId,
            CommentUserDto user,
            String comment,
            Long likes,
            Instant createdAt,
            Boolean isLiked
    ) {
        super(id, productId, user, comment, likes, createdAt);
        this.isLiked = isLiked;
    }
}
