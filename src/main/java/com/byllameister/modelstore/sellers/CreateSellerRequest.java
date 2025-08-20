package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.common.EnumValue;
import com.byllameister.modelstore.payments.PayoutMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSellerRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "payoutMethod is required")
    @EnumValue(enumClass = PayoutMethod.class, ignoreCase = true, message = "Payout method must be one of: YOOMONEY_WALLET, BANK_CARD")
    private String payoutMethod;

    @NotBlank(message = "payoutDestination is required and must be not blank")
    private String payoutDestination;
}
