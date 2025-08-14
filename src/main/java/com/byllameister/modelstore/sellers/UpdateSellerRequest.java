package com.byllameister.modelstore.sellers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSellerRequest {
    @NotNull(message = "payoutMethod is required")
    private PayoutMethod payoutMethod;

    @NotBlank(message = "payoutDestination is required and must be not blank")
    private String payoutDestination;
}
