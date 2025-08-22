package com.byllameister.modelstore.products;

public interface ProductWithSellerStatsFlatDto extends ProductFlatDto {
    Long getSales();
    Double getRevenue();
}
