package com.byllameister.modelstore.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private String previewImage;
    private String file;
}
