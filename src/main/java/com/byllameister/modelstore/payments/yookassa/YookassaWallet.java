package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.payments.PayoutDestination;
import com.byllameister.modelstore.payments.PayoutMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YookassaWallet implements PayoutDestination {
    private final String type = PayoutMethod.YOOMONEY_WALLET.getMethod();
    private String account_number;
}
