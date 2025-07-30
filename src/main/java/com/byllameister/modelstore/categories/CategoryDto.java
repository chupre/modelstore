package com.byllameister.modelstore.categories;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Title is required")
    private String name;
}
