package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.common.EnumValue;
import com.byllameister.modelstore.payments.PayoutMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSellerRequest {
    @NotNull(message = "payoutMethod is required")
    @EnumValue(enumClass = PayoutMethod.class, ignoreCase = true, message = "Payout method must be one of: YOOMONEY_WALLET, BANK_CARD")
    private PayoutMethod payoutMethod;

    @NotBlank(message = "payoutDestination is required and must be not blank")
    @Size(min = 13, max = 19, message = "payoutDestination must be between 13 and 19 digits")
    @Pattern(regexp = "\\d+", message = "payoutDestination must contain only digits")
    private String payoutDestination;
}
