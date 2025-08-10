package com.byllameister.modelstore.categories;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateCategoryRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;
}
