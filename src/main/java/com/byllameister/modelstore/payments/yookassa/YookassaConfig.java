package com.byllameister.modelstore.payments.yookassa;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class YookassaConfig {
    @Value("${yookassa.shop-id}")
    private String shopId;

    @Value("${yookassa.secret-key}")
    private String secretKey;

    private final String apiUrl = "https://api.yookassa.ru/v3/payments";
}
