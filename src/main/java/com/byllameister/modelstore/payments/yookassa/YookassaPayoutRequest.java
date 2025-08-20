package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.payments.PayoutDestination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YookassaPayoutRequest {
    private YookassaAmount amount;
    private PayoutDestination payout_destination_data;
}
