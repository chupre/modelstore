package com.byllameister.modelstore.products;

import java.time.LocalDate;

public interface ProductWithUserLikeFlatDto {
    Long getId();
    String getTitle();
    String getDescription();
    Double getPrice();
    String getPreviewImage();
    String getFile();
    Long getOwnerId();
    String getOwnerUsername();
    Long getCategoryId();
    String getCategoryName();
    Long getLikesCount();
    Boolean getIsLiked();
    LocalDate getCreatedAt();
}
