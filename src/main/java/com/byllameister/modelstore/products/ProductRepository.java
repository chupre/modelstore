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
}
