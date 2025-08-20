package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.payments.PayoutMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerResponse {
    private Long id;
    private Long userId;
    private PayoutMethod payoutMethod;
    private String payoutDestination;
}
