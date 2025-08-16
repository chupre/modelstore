package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.categories.CategoryDto;
import com.byllameister.modelstore.users.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ProductWithLikesDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String previewImage;
    private String file;
    private UserDto owner;
    private CategoryDto category;
    private Long likesCount;
    private LocalDate createdAt;
}