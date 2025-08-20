package com.byllameister.modelstore.payments.yookassa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YookassaAmount {
    private String value;
    private final String currency = "RUB";
}