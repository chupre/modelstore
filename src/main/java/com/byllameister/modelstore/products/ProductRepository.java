package com.byllameister.modelstore.products;

import com.byllameister.modelstore.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @NonNull
    @EntityGraph(attributePaths = {"owner", "category"})
    Page<Product> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"owner", "category"})
    List<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);

    List<Product> findByOwner(User user);
}
