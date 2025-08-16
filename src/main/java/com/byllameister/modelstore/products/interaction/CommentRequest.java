package com.byllameister.modelstore.products.interaction;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Comment is empty")
    @Max(value = 1000, message = "Comment is too big. Maximum is 1000 symbols")
    private String comment;
}
