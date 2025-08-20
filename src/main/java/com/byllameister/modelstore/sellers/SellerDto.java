package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.payments.PayoutMethod;
import lombok.Data;

@Data
public class SellerDto {
    private Long id;
    private Long userId;
    private PayoutMethod payoutMethod;
    private String payoutDestination;
}
