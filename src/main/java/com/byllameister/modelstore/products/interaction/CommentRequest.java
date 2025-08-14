package com.byllameister.modelstore.products.interaction;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    private String comment;
}
