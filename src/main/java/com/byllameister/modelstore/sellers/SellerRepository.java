package com.byllameister.modelstore.sellers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
        @Query("""
        SELECT new com.byllameister.modelstore.sellers.SellerWithStatsResponse(
            s.id,
            s.user.id,
            s.payoutMethod,
            s.payoutDestination,
    
            (SELECT COALESCE(SUM(p.amount), 0) FROM Payout p WHERE p.seller.id = s.id),
            (SELECT COUNT(DISTINCT oi) FROM OrderItem oi WHERE oi.product.owner.id = s.user.id AND oi.order.status = 'PAID'),
            (SELECT COUNT(DISTINCT pl) FROM ProductLike pl WHERE pl.product.owner.id = s.user.id),
            (SELECT COUNT(DISTINCT pr) FROM Product pr WHERE pr.owner.id = s.user.id)
        )
        FROM Seller s
        WHERE s.user.id = :userId
    """)
    Optional<SellerWithStatsResponse> findSellerWithStats(@Param("userId") Long userId);

    @Query("""
        SELECT new com.byllameister.modelstore.sellers.SellerWithStatsResponse(
            s.id,
            s.user.id,
            s.payoutMethod,
            s.payoutDestination,
    
            (SELECT COALESCE(SUM(p.amount), 0) FROM Payout p WHERE p.seller.id = s.id),
            (SELECT COUNT(DISTINCT oi) FROM OrderItem oi WHERE oi.product.owner.id = s.user.id AND oi.order.status = 'PAID'),
            (SELECT COUNT(DISTINCT pl) FROM ProductLike pl WHERE pl.product.owner.id = s.user.id),
            (SELECT COUNT(DISTINCT pr) FROM Product pr WHERE pr.owner.id = s.user.id)
        )
        FROM Seller s
    """)
    Page<SellerWithStatsResponse> findSellersWithStats(Pageable pageable);

    Optional<Seller> findByUserId(@Param("userId") Long userId);
    boolean existsByUserId(Long userId);
}