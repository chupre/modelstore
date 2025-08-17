package com.byllameister.modelstore.products;

import com.byllameister.modelstore.categories.CategoryDto;
import com.byllameister.modelstore.users.UserDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String previewImage;
    private String file;
    private UserDto owner;
    private CategoryDto category;
    private LocalDate createdAt;
}
