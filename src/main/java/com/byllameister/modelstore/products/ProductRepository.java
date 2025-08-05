package com.byllameister.modelstore.products;

import com.byllameister.modelstore.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @NonNull
    @EntityGraph(attributePaths = {"owner", "category"})
    Page<Product> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"owner", "category"})
    Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);

    @Modifying
    @EntityGraph(attributePaths = "category")
    @Query("DELETE FROM Product p WHERE p.owner = :owner")
    void deleteByOwner(@Param("owner") User user);

    @Query(value = """
            SELECT p.id as id,
                   p.title as title,
                   p.description as description,
                   p.price as price,
                   p.previewimage as previewImage,
                   p.file as file,
                   p.owner_id as ownerId,
                   p.category_id as categoryId,
                   p.createdat as createdAt
            FROM products p
            JOIN users u ON p.owner_id = u.id
            WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (
                :term IS NULL OR (
                    similarity(p.title, :term) > :threshold OR
                    similarity(p.description, :term) > :threshold OR
                    similarity(u.username, :term) > :threshold
                )
              )
            """,
            countQuery = """
                        SELECT COUNT(*) 
                        FROM products p
                        JOIN users u ON p.owner_id = u.id
                        WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
                          AND (:minPrice IS NULL OR p.price >= :minPrice)
                          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                          AND (
                            :term IS NULL OR (
                                similarity(p.title, :term) > :threshold OR
                                similarity(p.description, :term) > :threshold OR
                                similarity(u.username, :term) > :threshold
                            )
                          )
                    """,
            nativeQuery = true
    )
    Page<ProductFlatDto> fuzzySearch(
            @Param("term") String search,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("threshold") double threshold,
            Pageable pageable
    );
}
