package com.byllameister.modelstore.products.interaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {
    @Query("""
                SELECT new com.byllameister.modelstore.products.interaction.ProductCommentDto(
                            c.id,
                            c.product.id,
                            c.user.id,
                            c.comment,
                            COUNT(cl.id),
                            c.createdAt)
                FROM ProductComment c
                LEFT JOIN ProductCommentLike cl ON cl.comment.id = c.id
                WHERE c.product.id = :productId
                GROUP BY c.id
            """)
    Page<ProductCommentDto> findAll(@Param("productId") Long productId, Pageable pageable);

    @Query("""
                SELECT new com.byllameister.modelstore.products.interaction.CommentWithUserLikeResponse(
                            c.id,
                            c.product.id,
                            c.user.id,
                            c.comment,
                            COUNT(cl.id),
                            c.createdAt,
                            EXISTS(
                                SELECT 1 FROM ProductCommentLike pcl
                                WHERE pcl.comment.id = c.id
                                    AND pcl.user.id = :userId
                            )
                )
                FROM ProductComment c
                LEFT JOIN ProductCommentLike cl ON cl.comment.id = c.id
                WHERE c.product.id = :productId
                GROUP BY c.id
            """)
    Page<CommentWithUserLikeResponse> findAllWithUserLike(
            @Param("productId") Long productId,
            @Param("userId") Long userId,
            Pageable pageable);

    @Query("""
                SELECT new com.byllameister.modelstore.products.interaction.ProductCommentDto(
                            c.id,
                            c.product.id,
                            c.user.id,
                            c.comment,
                            COUNT(cl.id),
                            c.createdAt)
                FROM ProductComment c
                JOIN ProductCommentLike pcl ON pcl.comment.id = c.id AND pcl.user.id = :userId
                LEFT JOIN ProductCommentLike cl ON cl.comment.id = c.id
                GROUP BY c.id
            """)
    Page<ProductCommentDto> findLikedByUserId(Long userId, Pageable pageable);

    @Query(value = """
                SELECT new com.byllameister.modelstore.products.interaction.CommentWithUserLikeResponse(
                            c.id,
                            c.product.id,
                            c.user.id,
                            c.comment,
                            COUNT(cl.id),
                            c.createdAt,
                            EXISTS(
                                SELECT 1 FROM ProductCommentLike pcl
                                WHERE pcl.comment.id = c.id
                                    AND pcl.user.id = :userId
                            )
                )
                FROM ProductComment c
                LEFT JOIN ProductCommentLike cl ON cl.comment.id = c.id
                WHERE c.id = :commentId
                GROUP BY c.id
    """)
    Optional<CommentWithUserLikeResponse> findWithUserLike(
            @Param("commentId") Long commentId,
            @Param("userId") Long userId);
}