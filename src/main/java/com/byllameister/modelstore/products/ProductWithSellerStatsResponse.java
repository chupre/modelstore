package com.byllameister.modelstore.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithSellerStatsResponse extends ProductWithLikesResponse {
    private Long sales;
    private Double revenue;
}
