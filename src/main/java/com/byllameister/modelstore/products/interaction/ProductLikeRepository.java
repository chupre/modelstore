package com.byllameister.modelstore.products.interaction;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Long countAllByProductId(Long id);

    @Transactional
    @Modifying
    @Query("delete from ProductLike p where p.product.id = :productId and p.user.id = :userId")
    void deleteLike(@Param("productId") Long productId, @Param("userId") Long userId);

    @Query("select (count(p) > 0) from ProductLike p where p.product.id = :productId and p.user.id = :userId")
    boolean exists(@Param("productId") Long productId, @Param("userId") Long userId);

    @EntityGraph("product")
    List<ProductLike> findAllByUserId(Long userId);
}