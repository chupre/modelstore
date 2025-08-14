package com.byllameister.modelstore.products.interaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductCommentLikeRepository extends JpaRepository<ProductCommentLike, Long> {
    Long countAllByCommentId(Long commentId);

    @Transactional
    @Modifying
    @Query("delete from ProductCommentLike p where p.comment.id = :commentId and p.user.id = :userId")
    void deleteLike(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Query("select (count(p) > 0) from ProductCommentLike p where p.comment.id = :commentId and p.user.id = :userId")
    boolean exists(@Param("commentId") Long commentId, @Param("userId") Long userId);

    Long countByCommentId(Long commentId);
}