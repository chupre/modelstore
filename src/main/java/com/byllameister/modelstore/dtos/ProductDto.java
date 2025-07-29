package com.byllameister.modelstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Date;

@AllArgsConstructor
@Getter
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String previewImage;
    private String file;
    private Long ownerId;
    private Long categoryId;
    private Date createdAt;
}
