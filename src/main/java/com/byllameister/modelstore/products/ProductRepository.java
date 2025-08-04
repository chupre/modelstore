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

public interface ProductRepository extends JpaRepository<Product,Long> {
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
            SELECT p.id,
                   p.title,
                   p.description,
                   p.price,
                   p.previewimage,
                   p.file,
                   p.owner_id,
                   p.category_id,
                   p.createdat
            FROM products p
            JOIN users u ON p.owner_id = u.id
            WHERE similarity(p.title, :term) > :threshold
               OR similarity(p.description, :term) > :threshold
               OR similarity(u.username, :term) > :threshold
            """,
            nativeQuery = true,
            countQuery = """
                SELECT COUNT(*) FROM products p
                JOIN users u ON p.owner_id = u.id
                WHERE similarity(p.title, :term) > :threshold
                   OR similarity(p.description, :term) > :threshold
                   OR similarity(u.username, :term) > :threshold
            """
    )
    Page<Product> fuzzySearch(
            @Param("term") String search,
            @Param("threshold") double threshold,
            Pageable pageable);
}
