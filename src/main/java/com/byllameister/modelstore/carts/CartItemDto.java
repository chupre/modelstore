package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.products.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDto {
    private ProductDto product;
    private Boolean selected;
}
