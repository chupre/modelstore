package com.byllameister.modelstore.products;

import java.time.LocalDate;

public interface ProductFlatDto {
    Long getId();
    String getTitle();
    String getDescription();
    Double getPrice();
    String getPreviewImage();
    String getFile();
    Long getOwnerId();
    Long getCategoryId();
    LocalDate getCreatedAt();
}