package com.byllameister.modelstore.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithUserLikeResponse extends ProductWithLikesResponse {
    private Boolean isLiked;
}