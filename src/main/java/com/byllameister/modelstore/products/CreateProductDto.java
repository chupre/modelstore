package com.byllameister.modelstore.products;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface CreateProductDto {
    String getTitle();
    String getDescription();
    BigDecimal getPrice();
    MultipartFile getPreviewImage();
    MultipartFile getFile();
    Long getCategoryId();
}
