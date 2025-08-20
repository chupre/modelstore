package com.byllameister.modelstore.sellers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Query("""
        SELECT
            new com.byllameister.modelstore.sellers.SellerWithStatsResponse(
                s.id,
                s.user.id,
                s.payoutMethod,
                s.payoutDestination,
                COALESCE(SUM(p.amount), 0),
                null,
                null,
                null
            )
            FROM Seller s
            LEFT JOIN Payout p ON p.seller.id = s.id
            WHERE s.user.id = :userId
            GROUP BY s.id
    """)
    Optional<SellerWithStatsResponse> findSellerWithStats(@Param("userId") Long userId);

    @Query("""
        SELECT
            new com.byllameister.modelstore.sellers.SellerWithStatsResponse(
                s.id,
                s.user.id,
                s.payoutMethod,
                s.payoutDestination,
                null,
                null,
                null,
                null
            )
            FROM Seller s
    """)
    Page<SellerWithStatsResponse> findSellersWithStats(Pageable pageable);

    Optional<Seller> findByUserId(@Param("userId") Long userId);
    boolean existsByUserId(Long userId);
}