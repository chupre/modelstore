package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.payments.PayoutMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SellerWithStatsResponse {
    private Long id;
    private Long userId;
    private PayoutMethod payoutMethod;
    private String payoutDestination;

    private BigDecimal totalRevenue;
    private Long totalSales;
    private Long totalLikes;
    private Long totalProducts;
}
