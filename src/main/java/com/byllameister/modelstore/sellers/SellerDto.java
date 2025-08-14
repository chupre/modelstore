package com.byllameister.modelstore.sellers;

import lombok.Data;

@Data
public class SellerDto {
    private Long id;
    private Long userId;
    private PayoutMethod payoutMethod;
    private String payoutDestination;
}
