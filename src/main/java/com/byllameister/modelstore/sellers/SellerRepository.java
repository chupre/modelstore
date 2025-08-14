package com.byllameister.modelstore.sellers;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
  @EntityGraph(attributePaths = "user")
  Seller findByUserId(Long currentUserId);

  boolean existsByUserId(Long userId);
}