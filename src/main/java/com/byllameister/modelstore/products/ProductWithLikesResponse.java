package com.byllameister.modelstore.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithLikesResponse extends ProductDto {
    private Long likesCount;
}