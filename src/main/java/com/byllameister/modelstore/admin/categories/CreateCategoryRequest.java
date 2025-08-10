package com.byllameister.modelstore.admin.categories;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateCategoryRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;
}
