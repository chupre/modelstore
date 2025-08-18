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
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @NonNull
    @EntityGraph(attributePaths = {"owner", "category"})
    Page<Product> findAll(@NonNull Pageable pageable);

    @Modifying
    @EntityGraph(attributePaths = "category")
    @Query("DELETE FROM Product p WHERE p.owner = :owner")
    void deleteByOwner(@Param("owner") User user);

    @Query(
            value = """
                    SELECT * FROM (
                        SELECT p.id as id,
                               p.title as title,
                               p.description as description,
                               p.price as price,
                               p.previewimage as previewImage,
                               p.file as file,
                               u.id as ownerId,
                               u.username as ownerUsername,
                               c.id as categoryId,
                               c.name as categoryName,
                               p.createdat as createdAt,
                               COUNT(l.id) as likesCount
                        FROM products p
                        JOIN users u ON p.owner_id = u.id
                        JOIN categories c ON p.category_id = c.id
                        LEFT JOIN product_likes l ON l.product_id = p.id
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
                        GROUP BY p.id, u.id, c.id
                    ) sub
                    """,
            countQuery = """
                        SELECT COUNT(*) FROM products p
                        JOIN users u ON p.owner_id = u.id
                        JOIN categories c ON p.category_id = c.id
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

    @Query(value = """
                SELECT p.id,
                       p.title,
                       p.description,
                       p.price,
                       p.previewimage as previewImage,
                       p.file,
                       p.createdat as createdAt,
                       u.id as ownerId,
                       u.username as ownerUsername,
                       c.id as categoryId,
                       c.name as categoryName,
                       (SELECT COUNT(*) FROM product_likes l WHERE l.product_id = p.id) as likesCount
                FROM products p
                JOIN users u ON p.owner_id = u.id
                JOIN categories c ON p.category_id = c.id
                WHERE p.id = :productId
            """, nativeQuery = true)
    Optional<ProductFlatDto> findByIdWithLikes(@Param("productId") Long id);

    @Query(value = """
            SELECT * FROM (
                SELECT p.id as id,
                       p.title as title,
                       p.description as description,
                       p.price as price,
                       p.previewimage as previewImage,
                       p.file as file,
                       p.createdat as createdAt,
                       u.id as ownerId,
                       u.username as ownerUsername,
                       c.id as categoryId,
                       c.name as categoryName,
                       COUNT(l.id) as likesCount
                FROM products p
                JOIN users u ON p.owner_id = u.id
                JOIN categories c ON p.category_id = c.id
                LEFT JOIN product_likes l ON l.product_id = p.id
                WHERE p.owner_id = :sellerId
                GROUP BY p.id, u.id, c.id
            ) sub
            """,
            countQuery = """
                        SELECT COUNT(*)
                        FROM products p
                        WHERE p.owner_id = :sellerId
                    """,
            nativeQuery = true
    )
    Page<ProductFlatDto> findAllByOwnerIdWithLikes(@Param("sellerId") Long sellerId, Pageable pageable);


    @Query(value = """
                        SELECT * FROM (
                            SELECT p.id as id,
                                   p.title as title,
                                   p.description as description,
                                   p.price as price,
                                   p.previewimage as previewImage,
                                   p.file as file,
                                   u.id as ownerId,
                                   u.username as ownerUsername,
                                   c.id as categoryId,
                                   c.name as categoryName,
                                   p.createdat as createdAt,
                                   COUNT(l.id) as likesCount,
                                   EXISTS (
                                     SELECT 1 FROM product_likes pl
                                     WHERE pl.product_id = p.id AND pl.user_id = :userId
                                   ) as isLiked
                            FROM products p
                            JOIN users u ON p.owner_id = u.id
                            JOIN categories c ON p.category_id = c.id
                            LEFT JOIN product_likes l ON l.product_id = p.id
                            WHERE p.id = :productId
                            GROUP BY p.id, u.id, c.id
                        ) sub
            """,
            nativeQuery = true
    )
    Optional<ProductWithUserLikeFlatDto> findByIdWithUserLike(
            @Param("productId") Long productId,
            @Param("userId") Long userId
    );

    @Query(
            value = """
                    SELECT * FROM (
                        SELECT p.id as id,
                               p.title as title,
                               p.description as description,
                               p.price as price,
                               p.previewimage as previewImage,
                               p.file as file,
                               u.id as ownerId,
                               u.username as ownerUsername,
                               c.id as categoryId,
                               c.name as categoryName,
                               p.createdat as createdAt,
                               COUNT(l.id) as likesCount,
                               EXISTS (
                                 SELECT 1 FROM product_likes pl
                                 WHERE pl.product_id = p.id AND pl.user_id = :userId
                               ) as isLiked
                        FROM products p
                        JOIN users u ON p.owner_id = u.id
                        JOIN categories c ON p.category_id = c.id
                        LEFT JOIN product_likes l ON l.product_id = p.id
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
                        GROUP BY p.id, u.id, c.id
                    ) sub
                    """,
            countQuery = """
                        SELECT COUNT(*) FROM products p
                        JOIN users u ON p.owner_id = u.id
                        JOIN categories c ON p.category_id = c.id
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
    Page<ProductWithUserLikeFlatDto> fuzzySearchWithUserLike(
            @Param("term") String search,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("threshold") double threshold,
            @Param("userId")  Long userId,
            Pageable pageable
    );

    @Query(value = """
                SELECT * FROM (
                                SELECT p.id as id,
                                       p.title as title,
                                       p.description as description,
                                       p.price as price,
                                       p.previewimage as previewImage,
                                       p.file as file,
                                       u.id as ownerId,
                                       u.username as ownerUsername,
                                       c.id as categoryId,
                                       c.name as categoryName,
                                       p.createdat as createdAt,
                                       COUNT(l.id) as likesCount
                                FROM products p
                                JOIN product_likes pl ON pl.product_id = p.id
                                LEFT JOIN product_likes l ON l.product_id = p.id
                                JOIN users u ON p.owner_id = u.id
                                JOIN categories c ON p.category_id = c.id
                                WHERE pl.user_id = :userId
                                GROUP BY p.id, u.id, c.id
                            ) sub
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM (
                        SELECT p.id
                        FROM products p
                        JOIN product_likes pl ON pl.product_id = p.id
                        JOIN users u ON p.owner_id = u.id
                        JOIN categories c ON p.category_id = c.id
                        WHERE pl.user_id = :userId
                        GROUP BY p.id, u.id, c.id
                    ) sub
                    """,
            nativeQuery = true)
    Page<ProductFlatDto> findLikedByUserId(Long userId, Pageable pageable);
}
