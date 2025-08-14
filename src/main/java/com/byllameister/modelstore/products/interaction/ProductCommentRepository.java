package com.byllameister.modelstore.products.interaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {
    @Query("""
    SELECT new com.byllameister.modelstore.products.interaction.ProductCommentDto(c.id, c.product.id, c.user.id, c.comment, COUNT(cl.id), c.createdAt)
    FROM ProductComment c
    LEFT JOIN ProductCommentLike cl ON cl.comment.id = c.id
    WHERE c.product.id = :productId
    GROUP BY c.id
""")
    Page<ProductCommentDto> findAllWithLikes(@Param("productId") Long productId, Pageable pageable);
}