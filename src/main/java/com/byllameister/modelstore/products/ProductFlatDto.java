package com.byllameister.modelstore.products;

import java.time.LocalDateTime;

public interface ProductFlatDto {
    Long getId();
    String getTitle();
    String getDescription();
    Double getPrice();
    String getPreviewImage();
    String getFile();
    Long getOwnerId();
    Long getCategoryId();
    LocalDateTime getCreatedAt();
}